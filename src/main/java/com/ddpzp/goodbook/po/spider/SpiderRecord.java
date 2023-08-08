package com.ddpzp.goodbook.po.spider;

import lombok.Data;

import java.util.Date;

/**
 * 爬虫记录
 * Created by dd
 * Date 2019/8/22 19:49
 */
@Data
public class SpiderRecord {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 记录数
     */
    private Integer recordNum;
    /**
     * 记录类型：idiom
     */
    private String recordType;
    /**
     * 更新时间
     */
    private Date updateTime;
}
