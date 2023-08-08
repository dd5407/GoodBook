package com.ddpzp.goodbook.model.baiduHanyu;

import lombok.Data;

import java.util.List;

/**
 * 百度汉语成语结构
 * Created by dd
 * Date 2019/8/20 22:40
 */
@Data
public class BaiduHanyuIdiom {
    /**
     * 反义词
     */
    private List<String> antonym;
    /**
     * 百科id
     */
    private List<String> baike_id;
    /**
     * 成语源url，有两个url，第一个为成语详情页，第二个为成语出处列表页
     */
    private List<String> basic_source_url;
    /**
     * 释义
     */
    private List<String> definition;
    /**
     * 英语翻译
     */
    private List<String> english;
    /**
     * 例句
     */
    private List<String> liju;
    /**
     * 成语
     */
    private List<String> name;
    /**
     * 注音拼音
     */
    private List<String> pinyin;
    /**
     * 成语出处
     */
    private List<String> source;
    /**
     * 同义词成语
     */
    private List<String> synonym;
}
