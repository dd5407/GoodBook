package com.ddpzp.xiaogu_word.mapper.game;

import com.ddpzp.xiaogu_word.po.game.Idiom;

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
}
