package com.ddpzp.xiaogu_word.service.impl;

import com.ddpzp.xiaogu_word.exception.GbException;
import com.ddpzp.xiaogu_word.mapper.game.IdiomMapper;
import com.ddpzp.xiaogu_word.po.game.Frog;
import com.ddpzp.xiaogu_word.po.game.Idiom;
import com.ddpzp.xiaogu_word.service.GameService;
import com.ddpzp.xiaogu_word.util.IdiomCollection;
import com.ddpzp.xiaogu_word.util.NlpUtil;
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
        int randomIndex = (int) (Math.random() * (total));
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
        Idiom idiomInDatabase = idiomMapper.getIdiomByWord(word);
        if (idiomInDatabase != null) {
            throw new GbException("成语已存在！");
        }
        //使用hanLP转换拼音，保存进idiom
        NlpUtil.setIdiomPinyin(idiom);
        idiomMapper.addIdiom(idiom);
    }

}
