package com.ddpzp.xiaogu_word.service;

import com.ddpzp.xiaogu_word.common.Language;
import com.ddpzp.xiaogu_word.exception.GbException;
import com.ddpzp.xiaogu_word.model.game.RandomResult;
import com.ddpzp.xiaogu_word.po.game.Frog;
import com.ddpzp.xiaogu_word.po.game.GuessIdiom;
import com.ddpzp.xiaogu_word.po.game.Idiom;
import com.ddpzp.xiaogu_word.po.game.LotteryItem;

import java.util.List;

/**
 * Created by dd
 * Date 2019/8/12 20:05
 */
public interface GameService {
    /**
     * 数青蛙
     *
     * @param startNum
     * @param size
     * @return
     */
    List<Frog> countFrog(Integer startNum, Integer size);

    /**
     * 随机获取一个成语
     *
     * @return
     */
    Idiom randomIdiom() throws Exception;

    /**
     * 初始化成语，从网上爬取成语数据
     */
    void initIdiomData();

    /**
     * 添加成语
     *
     * @param idiom
     */
    void addIdiom(Idiom idiom) throws Exception;

    /**
     * 成语接龙
     *
     * @param queryWord 要接的成语，可以是完整成语，也可以是要接的字或该字的拼音
     * @param wordIndex 接第几个字
     * @return
     */
    Idiom idiomLoong(String queryWord, Integer wordIndex) throws GbException;

    /**
     * 猜成语-发送
     *
     * @param fromUsername
     * @param toUsername   对方用户名
     * @param guessIdiom   要给对方猜的成语
     */
    void sendIdiom(String fromUsername, String toUsername, String guessIdiom) throws GbException;

    /**
     * 猜成语
     *
     * @param id
     * @param idiom
     * @param username
     */
    void guessIdiom(Integer id, String idiom, String username) throws GbException;

    /**
     * 获取猜成语列表
     *
     * @param page
     * @param pageSize
     * @param username
     * @return
     */
    List<GuessIdiom> getGuessIdiomList(Integer page, Integer pageSize, String username);

    /**
     * 获取猜成语列表总数
     *
     * @param username
     * @return
     */
    Integer countGuessIdiom(String username);

    /**
     * 删除猜成语题目，只能删除自己出的题
     *
     * @param id
     * @param loginUser
     */
    void deleteGuessIdiom(Integer id, String loginUser) throws GbException;

    /**
     * 获取猜成语题目详情
     *
     * @param id
     * @return
     */
    GuessIdiom getGuessIdiomDetail(Integer id);

    /**
     * 放弃猜成语题目
     *
     * @param id
     * @param loginUser
     */
    void abandonGuessIdiom(Integer id, String loginUser) throws GbException;

    /**
     * 成语查询
     *
     * @param idiom
     * @return
     */
    Idiom queryIdiom(String idiom) throws GbException;

    /**
     * 获取抽奖列表
     *
     * @param username
     * @param current
     * @param pageSize
     * @return
     */
    List<LotteryItem> getLotteryItems(String username, Integer current, Integer pageSize);

    /**
     * 获取抽奖选项数量
     *
     * @param username
     * @return
     */
    Integer countLotteryItems(String username);

    /**
     * 根据选项名获取抽奖选项
     *
     * @param name
     * @param username
     * @return
     */
    List<LotteryItem> getLotteryItemByName(String name, String username);

    /**
     * 添加抽奖选项
     *
     * @param lotteryItem
     */
    void addLotteryItem(LotteryItem lotteryItem);

    /**
     * 根据id获取抽奖选项
     *
     * @param id
     * @return
     */
    LotteryItem getLotteryItem(Integer id);

    /**
     * 修改抽奖选项
     *
     * @param lotteryItem
     */
    void updateLotteryItem(LotteryItem lotteryItem);

    /**
     * 删除抽奖选项
     *
     * @param id
     */
    void deleteLotteryItem(Integer id);

    /**
     * 批量删除抽奖选项
     *
     * @param ids
     */
    void batchDeleteLottery(Integer[] ids);

    /**
     * 抽奖
     *
     * @param username
     * @return
     */
    LotteryItem lottery(String username) throws GbException;

    /**
     * 随机生成人名
     *
     * @param language 生成什么语言的人名
     * @return
     */
    RandomResult randomPersonName(Language language);
}
