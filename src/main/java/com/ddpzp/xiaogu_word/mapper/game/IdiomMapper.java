package com.ddpzp.xiaogu_word.mapper.game;

import com.ddpzp.xiaogu_word.po.game.Idiom;

import java.util.List;

/**
 * 成语mapper
 * Created by dd
 * Date 2019/8/18 3:30
 */
public interface IdiomMapper {
    /**
     * 获取成语总数
     *
     * @return
     */
    Integer countIdiom();

    /**
     * 获取第几个成语
     *
     * @param index 序号
     * @return
     */
    Idiom getIdiomByIndex(int index);

    /**
     * 添加成语
     *
     * @param idiom
     */
    void addIdiom(Idiom idiom);

    /**
     * 获取成语
     *
     * @param word
     * @return
     */
    Idiom getIdiomByWord(String word);

    /**
     * 查询以queryWord开头的词
     *
     * @param queryWord
     * @return
     */
    List<Idiom> idiomLoong(String queryWord);

    /**
     * 更新猜成语统计信息
     *
     * @param idiom
     */
    void updateGuessCount(Idiom idiom);
}
