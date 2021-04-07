package com.ddpzp.xiaogu_word.model.social;

import com.ddpzp.xiaogu_word.po.social.SlimRecord;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 减肥记录
 * Created by dd
 * Date 2021/4/6 20:28
 */
@Data
public class SlimRecordModel {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 早饭饱腹感
     * 0：没吃饭，1-10分饱
     */
    private Integer breakfastSatiety;
    /**
     * 午饭饱腹感
     * 0：没吃饭，1-10分饱
     */
    private Integer lunchSatiety;
    /**
     * 晚饭饱腹感
     * 0：没吃饭，1-10分饱
     */
    private Integer dinnerSatiety;
    /**
     * 运动次数
     * 0：没有运动
     */
    private Integer sport;
    /**
     * 0：没吃垃圾食品
     * 1：吃了垃圾食品
     */
    private Integer junkFood;
    /**
     * 今日体重
     */
    private Double weight;
    /**
     * 分数（花瓣数）
     */
    private Integer score;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 用户名
     */
    private String username;

    public static SlimRecordModel recordToModel(SlimRecord record) {
        if (record == null) {
            return null;
        }
        SlimRecordModel model = new SlimRecordModel();
        model.setId(record.getId());
        model.setBreakfastSatiety(record.getBreakfastSatiety());
        model.setLunchSatiety(record.getLunchSatiety());
        model.setDinnerSatiety(record.getDinnerSatiety());
        model.setSport(record.getSport());
        model.setJunkFood(record.getJunkFood());
        model.setWeight(record.getWeight());
        model.setScore(record.getScore());
        model.setCreateTime(record.getCreateTime());
        model.setUpdateTime(record.getUpdateTime());
        model.setUsername(record.getUsername());
        return model;
    }
}
