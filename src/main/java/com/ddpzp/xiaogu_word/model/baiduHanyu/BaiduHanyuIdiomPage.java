package com.ddpzp.xiaogu_word.model.baiduHanyu;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 百度汉语ajax请求返回的单页数据
 * Created by dd
 * Date 2019/8/20 22:37
 */
@Data
public class BaiduHanyuIdiomPage {
    /**
     * 成语总数
     */
    private Integer ret_num;
    /**
     * 成语列表
     */
    private List<BaiduHanyuIdiom> ret_array;
    /**
     * 分页信息，entity-num：总词数，return-num：每页个数，total-page：总页数
     */
    private Map<String,Integer> extra;

}
