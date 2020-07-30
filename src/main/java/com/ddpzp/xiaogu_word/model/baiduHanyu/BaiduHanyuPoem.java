package com.ddpzp.xiaogu_word.model.baiduHanyu;

import lombok.Data;

import java.util.List;

/**
 * 百度汉语诗词结构
 * Created by dd
 * Date 2019/8/20 22:40
 */
@Data
public class BaiduHanyuPoem {
    /**
     * 也就是诗词详情页链接里的pid
     */
    private List<String> sid;
    /**
     * 诗词名
     */
    private List<String> display_name;
    /**
     * 作者
     */
    private List<String> literature_author;
    /**
     * 朝代
     */
    private List<String> dynasty;
    /**
     * 诗词内容
     */
    private List<String> body;
}
