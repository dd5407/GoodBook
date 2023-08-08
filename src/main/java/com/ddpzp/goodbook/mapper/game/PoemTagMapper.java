package com.ddpzp.goodbook.mapper.game;

import com.ddpzp.goodbook.po.game.PoemTag;

/**
 * 诗词标签mapper
 * Created by dd
 * Date 2020/7/30 3:35
 */
public interface PoemTagMapper {
    /**
     * 添加标签
     *
     * @param tag
     */
    void addTag(PoemTag tag);
}
