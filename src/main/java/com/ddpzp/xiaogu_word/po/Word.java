package com.ddpzp.xiaogu_word.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 单词
 * Created by dd
 * Date 2019/7/25 0:25
 */
@Data
public class Word {
    private Integer id;
    private String english;
    private String chinese;
    /**
     * 音标
     */
    private String phonetic;
    /**
     * 例句
     */
    private String example;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    private String creator;
}
