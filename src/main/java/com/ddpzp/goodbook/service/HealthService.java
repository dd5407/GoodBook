package com.ddpzp.goodbook.service;

import com.ddpzp.goodbook.model.health.WeightModel;
import com.ddpzp.goodbook.po.health.Weight;

import java.util.List;

public interface HealthService {
    void add(Weight weight);

    List<WeightModel> getWeightRecordsByUser(Integer userId, Integer current, Integer pageSize, String timeRange);

    Integer getWeightTotalsByUser(Integer userId);

    void deleteWeightRecord(Integer id);

    void deleteWeightRecords(Integer[] ids);
}
