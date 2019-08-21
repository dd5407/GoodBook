package com.ddpzp.xiaogu_word.service.impl;

import com.alibaba.fastjson.JSON;
import com.ddpzp.xiaogu_word.exception.GbException;
import com.ddpzp.xiaogu_word.mapper.game.IdiomMapper;
import com.ddpzp.xiaogu_word.po.game.Frog;
import com.ddpzp.xiaogu_word.po.game.Idiom;
import com.ddpzp.xiaogu_word.service.GameService;
import com.ddpzp.xiaogu_word.util.IdiomCollection;
import com.ddpzp.xiaogu_word.util.NlpUtil;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        log.info("生成[0-{}]随机数：[{}]", total, randomIndex);
        return idiomMapper.getIdiomByIndex(randomIndex);
    }

    /**
     * 初始化成语，从网上爬取成语数据
     */
    @Override
    public void initIdiomData() {
        //百度爬虫
        idiomCollection.baiduParser();
        //百度汉语爬虫
        idiomCollection.hanyuIdiomSpider();
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
        } else if (queryWord.length() == 1) {//输入的是要接的字
            pinyin = HanLP.convertToPinyinString(queryWord, "", false);
        } else {//输入的是完整成语
            if (wordIndex == null || wordIndex > 4 || wordIndex <= 1) {
                throw new GbException(String.format("非法的序号[%s]，只能接第2-4字", wordIndex));
            }
            pinyin = HanLP.convertToPinyinList(queryWord).get(wordIndex - 1).getPinyinWithoutTone();
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
}
