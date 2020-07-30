package com.ddpzp.xiaogu_word.po.spider;

import lombok.Data;

import java.util.Date;

/**
 * 爬虫配置
 * TODO 与系统配置进行整合
 * Created by dd
 * Date 2020/7/31 1:22
 */
@Data
public class SpiderConfig {
    private Integer id;
    /**
     * 爬虫类型
     */
    private String type;
    /**
     * 配置选项
     */
    private String configName;
    /**
     * 选项值
     */
    private String configValue;
    private Date createTime;
}
