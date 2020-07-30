package com.ddpzp.xiaogu_word.mapper.spider;

import com.ddpzp.xiaogu_word.po.spider.SpiderConfig;
import org.apache.ibatis.annotations.Param;

/**
 * 爬虫配置
 * Created by dd
 * Date 2020/7/31 1:31
 */
public interface SpiderConfigMapper {
    /**
     * 获取爬虫配置
     *
     * @param type       爬虫类型
     * @param configName 爬虫选项
     * @return
     */
    SpiderConfig getConfig(@Param("type") String type, @Param("configName") String configName);
}
