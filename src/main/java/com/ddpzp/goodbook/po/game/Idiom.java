package com.ddpzp.goodbook.po.game;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 成语
 * Created by dd
 * Date 2019/8/18 3:12
 */
@Data
public class Idiom {
    private Integer id;
    private String word;
    /**
     * 成语释义
     */
    private String means;
    /**
     * 注音拼音
     */
    private String phoneticPinyin;
    private String firstPinyin;
    private String secondPinyin;
    private String thirdPinyin;
    private String fourthPinyin;
    /**
     * 分数，missCount/(missCount+passCount)
     */
    private Double score = 0.0;
    /**
     * 猜中次数
     */
    private Integer passCount = 0;
    /**
     * 没猜中次数
     */
    private Integer missCount = 0;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
