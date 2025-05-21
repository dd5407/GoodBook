package com.ddpzp.goodbook.service.impl;

import com.ddpzp.goodbook.mapper.health.WeightMapper;
import com.ddpzp.goodbook.model.health.WeightModel;
import com.ddpzp.goodbook.po.health.Weight;
import com.ddpzp.goodbook.po.user.User;
import com.ddpzp.goodbook.service.HealthService;
import com.ddpzp.goodbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HealthServiceImpl implements HealthService {
    @Autowired
    private WeightMapper weightMapper;
    @Autowired
    private UserService userService;

    @Override
    public void add(Weight weight) {
        Assert.notNull(weight.getWeight(), "体重不能为空！");
        weight.setCreateTime(new Date());
        weightMapper.addRecord(weight);
    }

    @Override
    public List<WeightModel> getWeightRecordsByUser(Integer userId, Integer current, Integer pageSize, String timeRange) {
        Integer startNum;
        if (current == null) {
            startNum = null;
        } else {
            startNum = (current - 1) * pageSize;
        }
        User user = userService.getUser(userId);
        String username = user.getUsername();
        List<Weight> weightList = weightMapper.getAllByUserId(userId, startNum, pageSize, timeRange);
        List<WeightModel> weightModels = new ArrayList<>();
        for (Weight weight : weightList) {
            weightModels.add(WeightModel.entityToModel(weight, username));
        }
        return weightModels;
    }

    @Override
    public Integer getWeightTotalsByUser(Integer userId) {
        return weightMapper.getTotalByUserId(userId);
    }

    @Override
    public void deleteWeightRecord(Integer id) {
        weightMapper.delete(id);
    }

    @Override
    public void deleteWeightRecords(Integer[] ids) {
        weightMapper.batchDelete(ids);
    }
}
