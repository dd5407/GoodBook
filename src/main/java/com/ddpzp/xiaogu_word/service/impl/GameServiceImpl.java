package com.ddpzp.xiaogu_word.service.impl;

import com.ddpzp.xiaogu_word.common.Constants;
import com.ddpzp.xiaogu_word.exception.GbException;
import com.ddpzp.xiaogu_word.mapper.game.GuessIdiomMapper;
import com.ddpzp.xiaogu_word.mapper.game.IdiomMapper;
import com.ddpzp.xiaogu_word.mapper.game.LotteryMapper;
import com.ddpzp.xiaogu_word.mapper.spider.SpiderRecordMapper;
import com.ddpzp.xiaogu_word.po.SpiderRecord;
import com.ddpzp.xiaogu_word.po.game.Frog;
import com.ddpzp.xiaogu_word.po.game.GuessIdiom;
import com.ddpzp.xiaogu_word.po.game.Idiom;
import com.ddpzp.xiaogu_word.po.game.LotteryItem;
import com.ddpzp.xiaogu_word.service.GameService;
import com.ddpzp.xiaogu_word.util.IdiomCollection;
import com.ddpzp.xiaogu_word.util.NlpUtil;
import com.hankcs.hanlp.HanLP;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dd
 * Date 2019/8/12 20:06
 */
@Service
@Slf4j
public class GameServiceImpl implements GameService {
    @Autowired
    private IdiomMapper idiomMapper;
    @Autowired
    private IdiomCollection idiomCollection;
    @Autowired
    private SpiderRecordMapper spiderRecordMapper;
    @Autowired
    private GuessIdiomMapper guessIdiomMapper;
    @Autowired
    private LotteryMapper lotteryMapper;

    @Override
    public List<Frog> countFrog(Integer startNum, Integer size) {
        List<Frog> list = new ArrayList<>();
        if (startNum == null || size == null) {
            return list;
        }
        for (int i = startNum; i < startNum + size; i++) {
            list.add(new Frog(i));
        }
        return list;
    }

    @Override
    public Idiom randomIdiom() throws Exception {
        Integer total = idiomMapper.countIdiom();
        if (total <= 0) {
            throw new GbException("数据库中没有成语！");
        }
        //生成一个0到total-1的随机数
        int randomIndex = (int) (Math.random() * total);
        Idiom randomIdiom = idiomMapper.getIdiomByIndex(randomIndex);
        log.info("生成[0-{}]随机数：[{}]，成语：[{}]", total, randomIndex, randomIdiom.getWord());
        return randomIdiom;
    }

    /**
     * 初始化成语，从网上爬取成语数据
     */
    @Override
    public void initIdiomData() {
        //判断是否需要启动爬虫
        if (!needStartSpider()) {
            return;
        }
        //百度爬虫
        idiomCollection.baiduParser();
        //百度汉语爬虫
        idiomCollection.hanyuIdiomSpider();
        //新增爬虫记录
        addSpiderRecord();
    }

    /**
     * 添加成语
     *
     * @param idiom
     */
    @Override
    public void addIdiom(Idiom idiom) throws Exception {
        String word = idiom.getWord();
        if (StringUtils.isBlank(word)) {
            throw new GbException("成语为空！");
        }
        if (word.length() != 4) {
            throw new GbException(String.format("[%s]不是四字成语！", word));
        }
        if (StringUtils.isBlank(idiom.getMeans()) || StringUtils.isBlank(idiom.getPhoneticPinyin())) {
            throw new GbException(String.format("成语[%s]注音拼音或释义为空！", word));
        }
        Idiom idiomInDatabase = idiomMapper.getIdiomByWord(word);
        if (idiomInDatabase != null) {
            throw new GbException(String.format("成语[%s]已存在！", word));
        }
        //使用hanLP转换拼音，保存进idiom
        NlpUtil.setIdiomPinyin(idiom);
        idiomMapper.addIdiom(idiom);
        log.info("Add idiom success! [{}]【拼音】：{} 【注释】：{}", word, idiom.getPhoneticPinyin(), idiom.getMeans());
    }

    /**
     * 成语接龙
     *
     * @param queryWord 要接的成语，可以是完整成语，也可以是要接的字或该字的拼音
     * @param wordIndex 接第几个字
     * @return
     */
    @Override
    public Idiom idiomLoong(String queryWord, Integer wordIndex) throws GbException {
        String pinyin;
        //输入的是要接的拼音
        if (NlpUtil.isPinyin(queryWord)) {
            pinyin = queryWord;
        } else if (queryWord.length() == 1 && NlpUtil.isChinese(queryWord)) {//输入的是要接的字
            pinyin = HanLP.convertToPinyinString(queryWord, "", false);
        } else if (NlpUtil.isFourIdiom(queryWord)) {//输入的是完整成语
            if (wordIndex == null || wordIndex > 4 || wordIndex <= 1) {
                throw new GbException(String.format("非法的序号[%s]，只能接第2-4字", wordIndex));
            }
            pinyin = HanLP.convertToPinyinList(queryWord).get(wordIndex - 1).getPinyinWithoutTone();
        } else {
            throw new GbException(String.format("[%s]什么鬼，看不懂！", queryWord));
        }
        List<Idiom> idiomList = idiomMapper.idiomLoong(pinyin);
        if (idiomList.size() == 0) {
            Idiom idiom = new Idiom();
            idiom.setWord("接不出来");
            idiom.setPhoneticPinyin("[ jiē bù chū lái ]");
            idiom.setMeans("出的成语太过惊世骇俗，让本系统无能为力时，显示的内容~");
            return idiom;
        }
        //产生0到size-1的序号
        int randomIndex = (int) (Math.random() * idiomList.size());
        //随机返回一个成语
        //todo 后期进行成语难度评级，按难度加权重进行随机，难度低的随机出现次数高，或者支持前台选择难度
        return idiomList.get(randomIndex);
    }

    /**
     * 猜成语-发送
     *
     * @param fromUsername
     * @param toUsername   对方用户名
     * @param guessIdiom   要给对方猜的成语
     */
    @Override
    public void sendIdiom(String fromUsername, String toUsername, String guessIdiom) throws GbException {
        Idiom idiom = idiomMapper.getIdiomByWord(guessIdiom);
        if (StringUtils.equals(fromUsername, toUsername)) {
            log.warn("Funny! fromUsername [{}] is equals toUsername", fromUsername);
            throw new GbException("自己给自己猜？有意思...不给猜！");
        }
        if (idiom == null) {
            log.warn("Idiom [{}] is not exist in database!", guessIdiom);
            throw new GbException(String.format("成语[%s]不存在，请重新输入！", guessIdiom));
        }
        guessIdiomMapper.sendIdiom(idiom.getWord(), idiom.getMeans(), fromUsername, toUsername);
    }

    /**
     * 猜成语
     *
     * @param id
     * @param idiom
     * @param username
     */
    @Override
    public void guessIdiom(Integer id, String idiom, String username) throws GbException {
        GuessIdiom guessIdiom = guessIdiomMapper.getGuessIdiom(id);
        if (guessIdiom == null) {
            log.error("Guess idiom is not exist! id={},idiom={},username={}", id, idiom, username);
            throw new GbException("系统错误！题目不存在，请刷新页面后再看！");
        }
        //检查当前用户是不是答题者
        if (!StringUtils.equals(username, guessIdiom.getToUsername())) {
            log.error("Current user [{}] is not toUser [{}]", username, guessIdiom.getToUsername());
            throw new GbException("系统错误，这道题好像不是你的哦？！");
        }

        if (Constants.GUESS_IDIOM_CREATED != guessIdiom.getIdiomStatus()) {
            log.warn("Guess idiom status error，idiomStatus={}.", guessIdiom.getIdiomStatus());
            throw new GbException("请不要重复提交！");
        }

        String word = guessIdiom.getWord();
        //答错
        if (!StringUtils.equals(idiom, word)) {
            updateGuessCount(word, false);
            log.warn("Guess idiom wrong! guess idiom:[{}],answer:[{}],username:[{}]", idiom, word, username);
            throw new GbException(String.format("啊哦，答错了！不是[%s]", idiom));
        }
        //答对
        updateGuessCount(word, true);
        guessIdiomMapper.updateStatus(id, Constants.GUESS_IDIOM_PASS);
    }

    /**
     * 获取猜成语列表
     *
     * @param page
     * @param pageSize
     * @param username
     * @return
     */
    @Override
    public List<GuessIdiom> getGuessIdiomList(Integer page, Integer pageSize, String username) {
        return guessIdiomMapper.getGuessIdiomList((page - 1) * pageSize, pageSize, username);
    }

    /**
     * 获取猜成语列表总数
     *
     * @param username
     * @return
     */
    @Override
    public Integer countGuessIdiom(String username) {
        return guessIdiomMapper.countGuessIdiom(username);
    }

    /**
     * 删除猜成语题目，只能删除自己出的题
     *
     * @param id
     * @param loginUser
     */
    @Override
    public void deleteGuessIdiom(Integer id, String loginUser) throws GbException {
        GuessIdiom guessIdiom = guessIdiomMapper.getGuessIdiom(id);
        if (guessIdiom == null) {
            throw new GbException("题目不存在，请刷新页面后再试！");
        }
        if (!StringUtils.equals(loginUser, guessIdiom.getFromUsername())) {
            throw new GbException("题目不是你出的，删不了，不好意思！");
        }
        guessIdiomMapper.deleteGuessIdiom(id);
    }

    /**
     * 获取猜成语题目详情
     *
     * @param id
     * @return
     */
    @Override
    public GuessIdiom getGuessIdiomDetail(Integer id) {
        return guessIdiomMapper.getGuessIdiom(id);
    }

    /**
     * 放弃猜成语题目
     *
     * @param id
     * @param loginUser
     */
    @Override
    public void abandonGuessIdiom(Integer id, String loginUser) throws GbException {
        GuessIdiom guessIdiom = guessIdiomMapper.getGuessIdiom(id);
        if (guessIdiom == null) {
            throw new GbException("题目不存在，请刷新页面后再试！");
        }
        if (!StringUtils.equals(loginUser, guessIdiom.getToUsername())) {
            throw new GbException("题目貌似不是给你答的？");
        }
        guessIdiomMapper.updateStatus(id, Constants.GUESS_IDIOM_ABANDON);
        log.info("Abandon guess idiom success！username={}", loginUser);
    }

    /**
     * 成语查询
     *
     * @param idiom
     * @return
     */
    @Override
    public Idiom queryIdiom(String idiom) throws GbException {
        Idiom result = idiomMapper.getIdiomByWord(idiom);
        if (result == null) {
            log.info("Query idiom is not exist! idiom=[{}]", idiom);
            throw new GbException("成语不存在！");
        }
        log.info("Query idiom success! idiom=[{}]", idiom);
        return result;
    }

    /**
     * 获取抽奖列表
     *
     * @param username
     * @param current
     * @param pageSize
     * @return
     */
    @Override
    public List<LotteryItem> getLotteryItems(String username, Integer current, Integer pageSize) {
        log.info("Get lottery item list. username={},current={},pageSize={}", username, current, pageSize);
        Integer startNum;
        if (current == null) {
            startNum = null;
        } else {
            startNum = (current - 1) * pageSize;
        }
        return lotteryMapper.getLotteryItems(username, startNum, pageSize);
    }

    /**
     * 获取抽奖选项数量
     *
     * @param username
     * @return
     */
    @Override
    public Integer countLotteryItems(String username) {
        return lotteryMapper.countLotteryItems(username);
    }

    /**
     * 根据选项名获取抽奖选项
     *
     * @param name
     * @param username
     * @return
     */
    @Override
    public List<LotteryItem> getLotteryItemByName(String name, String username) {
        return lotteryMapper.getLotteryItemByName(name, username);
    }

    /**
     * 添加抽奖选项
     *
     * @param lotteryItem
     */
    @Override
    public void addLotteryItem(LotteryItem lotteryItem) {
        lotteryMapper.addLotteryItem(lotteryItem);
    }

    /**
     * 根据id获取抽奖选项
     *
     * @param id
     * @return
     */
    @Override
    public LotteryItem getLotteryItem(Integer id) {
        return lotteryMapper.getLotteryItem(id);
    }

    /**
     * 修改抽奖选项
     *
     * @param lotteryItem
     */
    @Override
    public void updateLotteryItem(LotteryItem lotteryItem) {
        lotteryItem.setUpdateTime(new Date());
        lotteryMapper.updateLotteryItem(lotteryItem);
    }

    /**
     * 删除抽奖选项
     *
     * @param id
     */
    @Override
    public void deleteLotteryItem(Integer id) {
        lotteryMapper.deleteLotteryItem(id);
    }

    /**
     * 批量删除抽奖选项
     *
     * @param ids
     */
    @Override
    public void batchDeleteLottery(Integer[] ids) {
        lotteryMapper.batchDeleteLottery(ids);
    }

    /**
     * 抽奖
     *
     * @param username
     * @return
     */
    @Override
    public LotteryItem lottery(String username) throws GbException {
        Integer total = lotteryMapper.countLotteryItems(username);
        if (total <= 0) {
            throw new GbException("啥也没有，请添加选项！");
        }
        //生成一个0到total-1的随机数
        int randomIndex = (int) (Math.random() * total);
        LotteryItem lotteryItem = lotteryMapper.getLotteryItemByIndex(username, randomIndex);
        log.info("生成[0-{}]随机数：[{}]，抽奖选项名：[{}]", total, randomIndex, lotteryItem.getName());
        return lotteryItem;
    }

    /**
     * 根据猜的结果，更新统计信息
     *
     * @param word
     * @param isBingo
     */
    private void updateGuessCount(String word, boolean isBingo) {
        try {
            Idiom idiom = idiomMapper.getIdiomByWord(word);
            if (idiom == null) {
                return;
            }
            Integer passCount = idiom.getPassCount();
            Integer missCount = idiom.getMissCount();
            if (isBingo) {
                passCount++;
            } else {
                missCount++;
            }

            //计算难度系数分数
            double score = (double) missCount / (double) (missCount + passCount);
            idiom.setMissCount(missCount);
            idiom.setPassCount(passCount);
            idiom.setScore(score);
            idiomMapper.updateGuessCount(idiom);
            log.info("Update guess count! bingo={},passCount={},missCount={},score={},word={}",
                    isBingo, passCount, missCount, score, word);
        } catch (Exception e) {
            log.error("Update guess count failed！word={}", word, e);
        }
    }

    /**
     * 新增爬虫记录
     */
    private void addSpiderRecord() {
        try {
            Integer IdiomCount = idiomMapper.countIdiom();
            SpiderRecord record = new SpiderRecord();
            record.setRecordNum(IdiomCount);
            record.setRecordType(Constants.SPIDER_RECORD_TYPE_IDIOM);
            spiderRecordMapper.addRecord(record);
        } catch (Exception e) {
            log.error("Add spider record failed!", e);
        }
    }

    /**
     * 检查爬虫记录，决定是否需要启动爬虫
     *
     * @return
     */
    private boolean needStartSpider() {
        try {
            SpiderRecord spiderRecord = spiderRecordMapper.getLatestRecord(Constants.SPIDER_RECORD_TYPE_IDIOM);
            if (spiderRecord != null) {
                Integer lastCount = spiderRecord.getRecordNum();
                Integer idiomCount = idiomMapper.countIdiom();
                //当前记录数>=上次的记录，说明记录没丢失，不用重爬
                if (idiomCount >= lastCount) {
                    log.info("Last record num=[{}], current record num=[{}], no need start idiom spider!", lastCount, idiomCount);
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("Check spider record error！Continue to start spider...");
        }
        return true;
    }
}
