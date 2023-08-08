package com.ddpzp.goodbook.model.system;

import com.ddpzp.goodbook.exception.GbException;
import com.ddpzp.goodbook.po.system.SystemInformation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * 内存容量单位为MB，磁盘为GB
 * 使用率为百分比，所有小数保留两位
 * Created by dd
 * Date 2020/2/24 23:26
 */
@Data
public class SystemInfoModel {
    /**
     * cpu使用率
     */
    private Double cpuUsage;
    /**
     * 已使用虚拟内存
     */
    private Double swapMemoryUsed;
    /**
     * 总虚拟内存
     */
    private Double swapMemoryTotal;
    /**
     * 已使用内存
     */
    private Double memoryUsed;
    /**
     * 总内存
     */
    private Double memoryTotal;
    /**
     * 虚拟内存使用率
     */
    private Double swapMemoryUsage;
    /**
     * 内存使用率
     */
    private Double memoryUsage;
    /**
     * 已使用磁盘
     */
    private Double diskUsed;
    /**
     * 总磁盘容量
     */
    private Double diskTotal;
    /**
     * 磁盘使用率
     */
    private Double diskUsage;
    /**
     * 记录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 机器ip
     */
    private String ip;

    public static SystemInfoModel poToModel(SystemInformation systemInformation) throws GbException {
        SystemInfoModel systemInfoModel = new SystemInfoModel();
        systemInfoModel.setCpuUsage(setDoubleDigits(systemInformation.getCpuUsage(),2));
        systemInfoModel.setMemoryUsed(conversSize(systemInformation.getMemoryUsed(), "MB"));
        systemInfoModel.setMemoryTotal(conversSize(systemInformation.getMemoryTotal(), "MB"));
        systemInfoModel.setMemoryUsage(setDoubleDigits(systemInformation.getMemoryUsage(),2));
        systemInfoModel.setSwapMemoryUsed(conversSize(systemInformation.getSwapMemoryUsed(), "MB"));
        systemInfoModel.setSwapMemoryTotal(conversSize(systemInformation.getSwapMemoryTotal(), "MB"));
        systemInfoModel.setSwapMemoryUsage(setDoubleDigits(systemInformation.getSwapMemoryUsage(),2));
        systemInfoModel.setDiskUsed(conversSize(systemInformation.getDiskUsed(), "GB"));
        systemInfoModel.setDiskTotal(conversSize(systemInformation.getDiskTotal(), "GB"));
        systemInfoModel.setDiskUsage(setDoubleDigits(systemInformation.getDiskUsage(),2));
        systemInfoModel.setCreateTime(systemInformation.getCreateTime());
        return systemInfoModel;
    }

    private static String doubleToString(Double doubleValue) {
        if (doubleValue == null) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(doubleValue);
    }

    private static double setDoubleDigits(Double originValue, Integer digitsSize) throws GbException {
        if (originValue == null) {
            throw new GbException("originValue is null!");
        }
        if (digitsSize == null) {
            digitsSize = 2;
        }
        BigDecimal bigDecimal = new BigDecimal(originValue);
        double newVal = bigDecimal.setScale(digitsSize, BigDecimal.ROUND_HALF_UP).doubleValue();
        return newVal;
    }

    /**
     * 容量单位转换
     *
     * @param byteSize
     * @return
     */
    private static double conversSize(Long byteSize, String type) throws GbException {
        if (byteSize == null) {
            throw new GbException("byteSize is null！");
        }
        double size;
        if ("GB".equalsIgnoreCase(type)) {
            size = (double) byteSize / 1024 / 1024 / 1024;
        } else if ("MB".equalsIgnoreCase(type)) {
            size = (double) byteSize / 1024 / 1024;
        } else {
            size = byteSize;
        }

        return setDoubleDigits(size, 2);
    }
}
