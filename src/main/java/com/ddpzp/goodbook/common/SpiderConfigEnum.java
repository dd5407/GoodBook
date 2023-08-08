package com.ddpzp.goodbook.common;

/**
 * 爬虫配置项
 * Created by dd
 * Date 2020/7/31 2:12
 */
public enum SpiderConfigEnum {
    IDIOM_HANYU_ENABLE("idiom_hanyu", "enable"),
    IDIOM_BAIDU_ENABLE("idiom_baidu", "enable"),
    POEM_ENABLE("poem", "enable");

    SpiderConfigEnum(String type, String configName) {
        this.type = type;
        this.configName = configName;
    }

    private String type;
    private String configName;

    public String getType() {
        return type;
    }

    public String getConfigName() {
        return configName;
    }
}
