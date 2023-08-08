package com.ddpzp.goodbook.mapper.spider;

import com.ddpzp.goodbook.po.spider.SpiderRecord;

/**
 * Created by dd
 * Date 2019/8/22 19:45
 */
public interface SpiderRecordMapper {
    /**
     * 获取最新一条记录
     *
     * @param recordType
     * @return
     */
    SpiderRecord getLatestRecord(String recordType);

    /**
     * 新增爬虫记录
     *
     * @param record
     */
    void addRecord(SpiderRecord record);
}
