package com.ddpzp.xiaogu_word.service.impl;

import com.ddpzp.xiaogu_word.mapper.social.SlimMapper;
import com.ddpzp.xiaogu_word.model.social.SlimRecordModel;
import com.ddpzp.xiaogu_word.po.social.SlimRecord;
import com.ddpzp.xiaogu_word.service.SlimService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dd
 * Date 2021/4/6 22:16
 */
@Service
@Slf4j
public class SlimServiceImpl implements SlimService {
    @Autowired
    private SlimMapper slimMapper;

    @Override
    public List<SlimRecordModel> getRecords(String username, Integer current, Integer pageSize) {
        Integer startNum = null;
        if (current != null && pageSize != null) {
            startNum = (current - 1) * pageSize;
        }
        List<SlimRecord> records = slimMapper.getRecords(username, startNum, pageSize);
        return records.stream().map(SlimRecordModel::recordToModel).collect(Collectors.toList());
    }

    @Override
    public Integer getTatol(String username) {
        return slimMapper.getTotal(username);
    }

    @Override
    public List<SlimRecordModel> getRecordByDate(LocalDate queryDate, String username) {
        List<SlimRecord> records = slimMapper.getRecordsByDate(queryDate.toString(), username);
        return records.stream().map(SlimRecordModel::recordToModel).collect(Collectors.toList());
    }
}
