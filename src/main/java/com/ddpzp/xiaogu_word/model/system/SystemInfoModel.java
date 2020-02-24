package com.ddpzp.xiaogu_word.model.system;

import com.ddpzp.xiaogu_word.po.system.SystemInformation;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

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
    private String cpuUsage;
    /**
     * 已使用虚拟内存
     */
    private String swapMemoryUsed;
    /**
     * 总虚拟内存
     */
    private String swapMemoryTotal;
    /**
     * 已使用内存
     */
    private String memoryUsed;
    /**
     * 总内存
     */
    private String memoryTotal;
    /**
     * 虚拟内存使用率
     */
    private String swapMemoryUsage;
    /**
     * 内存使用率
     */
    private String memoryUsage;
    /**
     * 已使用磁盘
     */
    private String diskUsed;
    /**
     * 总磁盘容量
     */
    private String diskTotal;
    /**
     * 磁盘使用率
     */
    private String diskUsage;
    /**
     * 记录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 机器ip
     */
    private String ip;

    public static SystemInfoModel poToModel(SystemInformation systemInformation) {
        SystemInfoModel systemInfoModel = new SystemInfoModel();
        systemInfoModel.setCpuUsage(doubleToString(systemInformation.getCpuUsage()));
        systemInfoModel.setMemoryUsed(conversSize(systemInformation.getMemoryUsed(), "MB"));
        systemInfoModel.setMemoryTotal(conversSize(systemInformation.getMemoryTotal(), "MB"));
        systemInfoModel.setMemoryUsage(doubleToString(systemInformation.getMemoryUsage()));
        systemInfoModel.setSwapMemoryUsed(conversSize(systemInformation.getSwapMemoryUsed(), "MB"));
        systemInfoModel.setSwapMemoryTotal(conversSize(systemInformation.getSwapMemoryTotal(), "MB"));
        systemInfoModel.setSwapMemoryUsage(doubleToString(systemInformation.getSwapMemoryUsage()));
        systemInfoModel.setDiskUsed(conversSize(systemInformation.getDiskUsed(), "GB"));
        systemInfoModel.setDiskTotal(conversSize(systemInformation.getDiskTotal(), "GB"));
        systemInfoModel.setDiskUsage(doubleToString(systemInformation.getDiskUsage()));
        systemInfoModel.setCreateTime(systemInformation.getCreateTime());
        return systemInfoModel;
    }

    private static String doubleToString(Double doubleVal) {
        if (doubleVal == null) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(doubleVal);
    }

    /**
     * 容量单位转换
     *
     * @param byteSize
     * @return
     */
    private static String conversSize(Long byteSize, String type) {
        if (byteSize == null) {
            return null;
        }
        double size;
        if ("GB".equalsIgnoreCase(type)) {
            size = (double) byteSize / 1024 / 1024 / 1024;
        } else if ("MB".equalsIgnoreCase(type)) {
            size = (double) byteSize / 1024 / 1024;
        } else {
            return String.valueOf(byteSize);
        }

        return doubleToString(size);
    }
}
