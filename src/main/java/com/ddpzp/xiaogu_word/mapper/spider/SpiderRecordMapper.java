package com.ddpzp.xiaogu_word.mapper.spider;

import com.ddpzp.xiaogu_word.po.SpiderRecord;

/**
 * Created by dd
 * Date 2019/8/22 19:45
 */
public interface SpiderRecordMapper {
    /**
     * 获取最新一条记录
     *
     * @param spiderRecordTypeIdiom
     * @return
     */
    SpiderRecord getLatestRecord(String spiderRecordTypeIdiom);

    /**
     * 新增爬虫记录
     *
     * @param record
     */
    void addRecord(SpiderRecord record);
}
