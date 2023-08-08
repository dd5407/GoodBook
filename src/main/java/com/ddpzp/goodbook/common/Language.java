package com.ddpzp.goodbook.common;

import java.util.Locale;

/**
 * 随机生成器所使用的语言
 * Created by dd
 * Date 2020/7/2 22:42
 */
public enum Language {
    CHINESE("chinese", Locale.CHINA),
    ENGLISH("english", Locale.ENGLISH);

    private String name;
    private Locale locale;

    Language(String name, Locale locale) {
        this.name = name;
        this.locale = locale;
    }

    public String getName() {
        return name;
    }

    public Locale getLocale() {
        return locale;
    }
}
