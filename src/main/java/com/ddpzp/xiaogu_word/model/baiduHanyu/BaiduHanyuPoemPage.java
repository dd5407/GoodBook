package com.ddpzp.xiaogu_word.model.baiduHanyu;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 百度汉语-诗词ajax请求返回的单页数据
 * Created by dd
 * Date 2019/8/20 22:37
 */
@Data
public class BaiduHanyuPoemPage {
    /**
     * 诗词总数
     */
    private Integer ret_num;
    /**
     * 诗词列表
     */
    private List<BaiduHanyuPoem> ret_array;
    /**
     * 分页信息，entity-num：诗词总数，total-page：总页数
     */
    private Map<String,Integer> extra;

}
