package com.ddpzp.goodbook.po.health;

import com.ddpzp.goodbook.po.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 日常体重记录
 */
@Data
public class Weight extends BaseEntity {
    /**
     * 体重
     */
    private Double weight;
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 测量时间，一般与创建时间相同，可修改
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date recordTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
