package com.ddpzp.xiaogu_word.service;

import com.ddpzp.xiaogu_word.model.social.SlimRecordModel;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by dd
 * Date 2021/4/6 22:16
 */
public interface SlimService {
    List<SlimRecordModel> getRecords(String username, Integer current, Integer pageSize);

    Integer getTatol(String username);

    /**
     * 根据日期获取瘦身记录
     *
     * @param queryDate
     * @param username
     * @return
     */
    List<SlimRecordModel> getRecordByDate(LocalDate queryDate, String username);
}
