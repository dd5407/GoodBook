package com.ddpzp.goodbook.model.health;

import com.ddpzp.goodbook.po.health.Weight;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class WeightModel {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 体重
     */
    private Double weight;
    /**
     * 用户id
     */
    private String userName;
    /**
     * 测量时间，一般与创建时间相同，可修改
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date recordTime;
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

    public static WeightModel entityToModel(Weight weight, String userName) {
        if (weight == null) {
            return null;
        }
        WeightModel weightModel = new WeightModel();
        weightModel.setId(weight.getId());
        weightModel.setUserName(userName);
        weightModel.setWeight(weight.getWeight());
        weightModel.setCreateTime(weight.getCreateTime());
        weightModel.setRecordTime(weight.getRecordTime());
        weightModel.setUpdateTime(weight.getUpdateTime());
        return weightModel;
    }

    public static Weight modelToEntity(WeightModel weightModel, Integer userId) {
        if (weightModel == null) {
            return null;
        }
        Weight weight = new Weight();
        weight.setId(weightModel.getId());
        weight.setUserId(userId);
        weight.setWeight(weightModel.getWeight());
        weight.setCreateTime(weightModel.getCreateTime());
        weight.setRecordTime(weightModel.getRecordTime());
        weight.setUpdateTime(weightModel.getUpdateTime());
        return weight;
    }
}
