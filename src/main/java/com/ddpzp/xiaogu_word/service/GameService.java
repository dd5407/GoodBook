package com.ddpzp.xiaogu_word.service;

import com.ddpzp.xiaogu_word.exception.GbException;
import com.ddpzp.xiaogu_word.po.game.Frog;
import com.ddpzp.xiaogu_word.po.game.Idiom;

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
}
