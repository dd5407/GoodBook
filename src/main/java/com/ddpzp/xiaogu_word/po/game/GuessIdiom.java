package com.ddpzp.xiaogu_word.po.game;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 猜成语
 * Created by dd
 * Date 2019/8/25 18:32
 */
@Data
public class GuessIdiom {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 成语
     */
    private String word;
    /**
     * 释义
     */
    private String means;
    /**
     * 出题者、发送人
     */
    private String fromUsername;
    /**
     * 答题者、接收人
     */
    private String toUsername;
    /**
     * 成语状态，0-待猜，1-猜中，2-未猜中
     */
    private Integer idiomStatus;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
