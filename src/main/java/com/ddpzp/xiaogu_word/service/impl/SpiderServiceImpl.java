package com.ddpzp.xiaogu_word.service.impl;

import com.ddpzp.xiaogu_word.common.Constants;
import com.ddpzp.xiaogu_word.common.SpiderConfigEnum;
import com.ddpzp.xiaogu_word.mapper.game.IdiomMapper;
import com.ddpzp.xiaogu_word.mapper.game.PoemMapper;
import com.ddpzp.xiaogu_word.mapper.spider.SpiderConfigMapper;
import com.ddpzp.xiaogu_word.mapper.spider.SpiderRecordMapper;
import com.ddpzp.xiaogu_word.po.spider.SpiderConfig;
import com.ddpzp.xiaogu_word.po.spider.SpiderRecord;
import com.ddpzp.xiaogu_word.service.SpiderService;
import com.ddpzp.xiaogu_word.util.spider.IdiomSpider;
import com.ddpzp.xiaogu_word.util.spider.PoemSpider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.Element;

/**
 * Created by dd
 * Date 2020/7/31 2:47
 */
@Service
@Slf4j
public class SpiderServiceImpl implements SpiderService {
    @Autowired
    private SpiderConfigMapper spiderConfigMapper;
    @Autowired
    private SpiderRecordMapper spiderRecordMapper;
    @Autowired
    private PoemMapper poemMapper;
    @Autowired
    private IdiomMapper idiomMapper;
    @Autowired
    private PoemSpider poemSpider;
    @Autowired
    private IdiomSpider idiomSpider;


    /**
     * 初始化成语，从网上爬取成语数据
     */
    @Override
    public void initIdiomData() {
        try {
            if (!missingData(Constants.SPIDER_RECORD_TYPE_IDIOM)) {
                return;
            }
            boolean baiduEnable = validateConfig(SpiderConfigEnum.IDIOM_BAIDU_ENABLE, Constants.SPIDER_CONFIG_ENABLE);
            boolean hanyuEnable = validateConfig(SpiderConfigEnum.IDIOM_HANYU_ENABLE, Constants.SPIDER_CONFIG_ENABLE);
            //百度爬虫/
            if (baiduEnable) {
                idiomSpider.baiduParser();
            }
            //百度汉语爬虫
            if (hanyuEnable) {
                idiomSpider.hanyuIdiomSpider();
            }
            //新增爬虫记录
            if (baiduEnable && hanyuEnable) {
                addSpiderRecord(Constants.SPIDER_RECORD_TYPE_IDIOM);
            }
        } catch (Exception e) {
            log.error("初始化成语爬虫失败！", e);
        }
    }

    @Override
    public void initPoemData() {
        try {
            boolean enable = validateConfig(SpiderConfigEnum.POEM_ENABLE, Constants.SPIDER_CONFIG_ENABLE);
            if (enable && missingData(Constants.SPIDER_RECORD_TYPE_POEM)) {
                poemSpider.collect();
                addSpiderRecord(Constants.SPIDER_RECORD_TYPE_POEM);
            }
        } catch (Exception e) {
            log.error("初始化诗词爬虫失败！", e);
        }
    }

    /**
     * 检查数据库中的数据是否缺失
     * 两种情况会启动爬虫：1.没有爬虫记录。2.表中数据少于爬虫记录中记录的值。
     *
     * @param spiderType 爬虫类型
     * @return
     */
    private boolean missingData(String spiderType) throws Exception {
        SpiderRecord record = spiderRecordMapper.getLatestRecord(spiderType);
        if (record == null) {
            log.info("No {} spider record! Will start spider.", spiderType);
            return true;
        }
        Integer count = countData(spiderType);
        Integer lastRecordNum = record.getRecordNum();
        if (count < lastRecordNum) {
            log.info("Last record num=[{}], current record num=[{}], need start {} spider!",
                    lastRecordNum, count, spiderType);
            return true;
        }
        log.info("Last record num=[{}], current record num=[{}], no need start {} spider!",
                lastRecordNum, count, spiderType);
        return false;
    }

    /**
     * 校验配置是否和目标值匹配
     *
     * @param config
     * @param value  目标值
     * @return
     * @throws Exception
     */
    private boolean validateConfig(SpiderConfigEnum config, String value) throws Exception {
        SpiderConfig dbConfig = spiderConfigMapper.getConfig(config.getType(), config.getConfigName());
        if (dbConfig == null) {
            log.warn("找不到配置：[type:{},configName:{}]", config.getType(), config.getConfigName());
            return false;
        }
        log.info("Validate config:[type:{},configName:{},configValue:{}],targetValue:{}.",
                config.getType(), config.getConfigName(), dbConfig.getConfigValue(), value);
        return StringUtils.equals(value, dbConfig.getConfigValue());
    }

    /**
     * 新增爬虫记录
     */
    private void addSpiderRecord(String type) {
        try {
            Integer count = countData(type);

            SpiderRecord record = new SpiderRecord();
            record.setRecordNum(count);
            record.setRecordType(type);
            spiderRecordMapper.addRecord(record);
            log.info("Spider record added! type:{}, count:{}", type, count);
        } catch (Exception e) {
            log.error("Add spider record failed!", e);
        }
    }

    private Integer countData(String spiderType) throws Exception {
        switch (spiderType) {
            case Constants.SPIDER_RECORD_TYPE_IDIOM:
                return idiomMapper.countIdiom();
            case Constants.SPIDER_RECORD_TYPE_POEM:
                return poemMapper.countPoem();
            default:
                throw new Exception(String.format("Query %s count failed! Invalid spiderType:%s.", spiderType, spiderType));
        }
    }
}
