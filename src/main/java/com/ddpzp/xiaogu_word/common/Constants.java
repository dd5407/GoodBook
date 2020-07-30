package com.ddpzp.xiaogu_word.common;

/**
 * Created by dd
 * Date 2019/7/30 22:34
 */
public class Constants {
    public static final String SESSION_USER_KEY = "loginUser";
    public static final String ERROR_MSG_KEY = "errMsg";

    /* 爬虫类型 */
    //成语
    public static final String SPIDER_RECORD_TYPE_IDIOM = "idiom";
    //诗词
    public static final String SPIDER_RECORD_TYPE_POEM = "poem";

    /* 爬虫配置 */
    //开启
    public static final String SPIDER_CONFIG_ENABLE = "true";
    //关闭
    public static final String SPIDER_CONFIG_DISABLE = "false";

    /* 猜成语 */
    //未猜
    public static final int GUESS_IDIOM_CREATED = 0;
    //猜中
    public static final int GUESS_IDIOM_PASS = 1;
    //放弃
    public static final int GUESS_IDIOM_ABANDON = 2;
}
