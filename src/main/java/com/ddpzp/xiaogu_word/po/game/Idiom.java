package com.ddpzp.xiaogu_word.po.game;

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
    private String describe;
    private String firstPinyin;
    private String secondPinyin;
    private String thirdPinyin;
    private String fourthPinyin;
    /**
     * 难度，数字越大难度越大
     */
    private Integer level;
    /**
     * 猜中次数
     */
    private Integer passCount;
    /**
     * 没猜中次数
     */
    private Integer missCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
