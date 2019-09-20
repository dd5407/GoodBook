package com.ddpzp.xiaogu_word.service;

import com.ddpzp.xiaogu_word.exception.GbException;
import com.ddpzp.xiaogu_word.po.game.Frog;
import com.ddpzp.xiaogu_word.po.game.GuessIdiom;
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
     * @param idiom
     * @return
     */
    Idiom queryIdiom(String idiom) throws GbException;
}
