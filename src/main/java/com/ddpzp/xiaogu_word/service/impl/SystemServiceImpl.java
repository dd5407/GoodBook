package com.ddpzp.xiaogu_word.service.impl;

import com.ddpzp.xiaogu_word.exception.GbException;
import com.ddpzp.xiaogu_word.mapper.system.SystemConfigMapper;
import com.ddpzp.xiaogu_word.mapper.system.SystemInfoMapper;
import com.ddpzp.xiaogu_word.po.system.SystemConfig;
import com.ddpzp.xiaogu_word.po.system.SystemInformation;
import com.ddpzp.xiaogu_word.service.SystemService;
import com.ddpzp.xiaogu_word.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by dd
 * Date 2020/2/21 23:03
 */
@Service
@Slf4j
public class SystemServiceImpl implements SystemService {
    /**
     * 系统监控数据默认最大保存天数30天
     */
    public static final int SYSTEM_INFO_MAX_SAVE_DAY_DEFAULT = 30;
    @Autowired
    private SystemInfoMapper systemInfoMapper;
    @Autowired
    private SystemConfigMapper systemConfigMapper;

    /**
     * 定时调度
     * 每30秒记录一次
     */
    @Scheduled(cron = "*/30 * * * * ?")
    public void collectionSchedule() {
        addSystemInfo();
    }

    /**
     * 每天凌晨两点清理一次系统监控数据
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void clearSchedule() {
        clearSystemInfoRecords();
    }

    /**
     * 添加系统信息
     */
    @Override
    public void addSystemInfo() {
        if (!enableCollection()) {
            log.info("Skip collect system info!");
            return;
        }
        try {
            log.info("Start collect system info...");
            SystemInformation systemInformation = collectSystemInfo();
            systemInfoMapper.addSystemInfo(systemInformation);
            log.info("End collect system info...");
        } catch (Exception e) {
            log.error("Add system info failed !", e);
        }
    }

    /**
     * 获取本机的监控记录
     *
     * @return
     */
    @Override
    public List<SystemInformation> getLocalSystemInfoRecords() {
        return systemInfoMapper.getSystemInfoRecords(SystemUtil.getLocalIp());
    }

    /**
     * 获取本机最近一次监控数据
     *
     * @return
     */
    @Override
    public SystemInformation getLatestSystemInfoRecord() {
        return systemInfoMapper.getLatestSystemInfoRecord(SystemUtil.getLocalIp());
    }

    /**
     * 获取系统信息
     *
     * @return
     */
    @Override
    public List<SystemInformation> getAllSystemInfoRecords() {
        return systemInfoMapper.getSystemInfoRecords(null);
    }

    /**
     * 根据指定ip获取监控配置
     *
     * @param ip
     * @return
     */
    public SystemConfig getSystemConfig(String ip) throws GbException {
        if (StringUtils.isBlank(ip)) {
            throw new GbException("IP is empty！");
        }
        return systemConfigMapper.getConfig(ip);
    }

    /**
     * 清理监控数据
     */
    @Override
    public void clearSystemInfoRecords() {
        try {
            SystemConfig config = getLocalSystemConfig();
            if (config == null) {
                return;
            }
            Integer systemInfoMaxSaveDay = config.getSystemInfoMaxSaveDay();
            if (systemInfoMaxSaveDay == null || systemInfoMaxSaveDay <= 0) {
                systemInfoMaxSaveDay = SYSTEM_INFO_MAX_SAVE_DAY_DEFAULT;
            }
            log.info("Begin clear system info...maxSaveDay={}", systemInfoMaxSaveDay);
            deleteSystemInfo(systemInfoMaxSaveDay);
            log.info("Clear system info complete！");
        } catch (GbException e) {
            log.error("Clear system info error!", e);
        }
    }

    /**
     * 获取本机监控配置
     *
     * @return
     */
    public SystemConfig getLocalSystemConfig() throws GbException {
        String localIp = SystemUtil.getLocalIp();
        return getSystemConfig(localIp);
    }

    /**
     * 删除N天以前的监控数据
     *
     * @param maxSaveDays
     */
    private void deleteSystemInfo(Integer maxSaveDays) {
        //删除N-1天0点0分0秒前的数据
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - maxSaveDays + 1);
        Date date = calendar.getTime();
        String localIp = SystemUtil.getLocalIp();
        systemInfoMapper.deleteRecordBeforeDate(localIp, date);
    }

    /**
     * 检查本机是否开启监控
     *
     * @return
     */
    private boolean enableCollection() {
        SystemConfig config = null;
        try {
            config = getLocalSystemConfig();
        } catch (GbException e) {
            log.error("Get local system config failed!", e);
        }

        if (config == null) {
            return false;
        }
        return config.getEnableSystemInfoCollection();
    }

    private SystemInformation collectSystemInfo() {
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        HardwareAbstractionLayer hardware = systemInfo.getHardware();

        double systemCpuLoad = hardware.getProcessor().getSystemCpuLoad();
        OSFileStore[] fileStores = operatingSystem.getFileSystem().getFileStores();
        long diskUsable = 0L;
        long diskTotal = 0L;
        for (OSFileStore osf : fileStores) {
            diskTotal += osf.getTotalSpace();
            diskUsable += osf.getUsableSpace();
        }
        long diskUsed = diskTotal - diskUsable;

        GlobalMemory memory = hardware.getMemory();
        long memoryAvailable = memory.getAvailable();
        long memoryTotal = memory.getTotal();
        long memoryUsed = memoryTotal - memoryAvailable;
        long swapUsed = memory.getSwapUsed();
        long swapTotal = memory.getSwapTotal();

        SystemInformation systemInformation = new SystemInformation();
        systemInformation.setIp(SystemUtil.getLocalIp());
        systemInformation.setCpuUsage(systemCpuLoad);

        systemInformation.setMemoryUsed(memoryUsed);
        systemInformation.setMemoryTotal(memoryTotal);
        systemInformation.calcMemoryUsage();

        systemInformation.setSwapMemoryUsed(swapUsed);
        systemInformation.setSwapMemoryTotal(swapTotal);
        systemInformation.calcSwapMemoryUsage();

        systemInformation.setDiskUsed(diskUsed);
        systemInformation.setDiskTotal(diskTotal);
        systemInformation.calcDiskUsage();

        return systemInformation;
    }

}
