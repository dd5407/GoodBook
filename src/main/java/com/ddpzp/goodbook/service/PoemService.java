package com.ddpzp.goodbook.service;

import com.ddpzp.goodbook.exception.GbException;
import com.ddpzp.goodbook.po.game.Poem;

import java.util.List;
import java.util.Set;

/**
 * 诗词
 * Created by dd
 * Date 2020/8/11 22:25
 */
public interface PoemService {
    /**
     * 添加诗词
     *
     * @param poem 诗词
     * @param tags 标签
     */
    void addPoem(Poem poem, Set<String> tags) throws GbException;

    /**
     * 模糊查询诗词
     *
     * @param title
     * @param author
     * @param body
     * @param current
     * @param pageSize
     * @return
     * @throws GbException
     */
    List<Poem> fuzzySearchPoem(String title, String author, String body, Integer current, Integer pageSize);

    /**
     * 模糊查询诗词，查询总数
     *
     * @param title
     * @param author
     * @param body
     * @return
     */
    Integer countFuzzySearchPoem(String title, String author, String body);

    /**
     * 全匹配查询诗词
     *
     * @param title
     * @param author
     * @param current
     * @param pageSize
     * @return
     * @throws GbException
     */
    List<Poem> searchPoem(String title, String author, Integer current, Integer pageSize);

    /**
     * 全匹配查询诗词，查询总数
     *
     * @param title
     * @param author
     * @return
     */
    Integer countSearchPoem(String title, String author);
}
