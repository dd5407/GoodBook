package com.ddpzp.xiaogu_word.service;

import com.ddpzp.xiaogu_word.exception.GbException;
import com.ddpzp.xiaogu_word.model.system.SystemInfoModel;
import com.ddpzp.xiaogu_word.po.system.SystemInformation;
import com.ddpzp.xiaogu_word.po.system.SystemConfig;

import java.util.List;

/**
 * Created by dd
 * Date 2020/2/21 23:02
 */
public interface SystemService {
    /**
     * 添加系统信息
     */
    void addSystemInfo();

    /**
     * 获取系统信息
     *
     * @return
     */
    List<SystemInformation> getAllSystemInfoRecords();

    /**
     * 获取本机系统信息
     *
     * @param page
     * @param pageSize
     * @return
     * @throws GbException
     */
    List<SystemInfoModel> getLocalSystemInfoRecords(Integer page, Integer pageSize) throws GbException;

    /**
     * 获取本机最近一次监控数据
     *
     * @return
     */
    SystemInfoModel getLatestSystemInfoRecord() throws GbException;

    /**
     * 根据指定ip获取监控配置
     *
     * @param ip
     * @return
     */
    SystemConfig getSystemConfig(String ip) throws GbException;

    /**
     * 清理监控数据
     */
    void clearSystemInfoRecords();
}
