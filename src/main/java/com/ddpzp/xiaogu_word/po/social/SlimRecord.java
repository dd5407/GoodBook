package com.ddpzp.xiaogu_word.po.social;

import lombok.Data;

import java.util.Date;

/**
 * 减肥记录
 * Created by dd
 * Date 2021/4/6 20:28
 */
@Data
public class SlimRecord {
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
     * 分数
     */
    private Integer score;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 用户名
     */
    private String username;
}
