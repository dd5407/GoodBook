package com.ddpzp.goodbook.service;

/**
 * Created by dd
 * Date 2020/7/31 2:47
 */
public interface SpiderService {
    /**
     * 初始化成语，从网上爬取成语数据
     */
    void initIdiomData();

    /**
     * 初始化诗词数据
     */
    void initPoemData();
}
