package com.ddpzp.goodbook.po.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 容量单位都为bytes
 * 使用率为百分比
 * Created by dd
 * Date 2020/2/21 23:06
 */
@Data
public class SystemInformation {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * cpu使用率
     */
    private Double cpuUsage;
    /**
     * 已使用虚拟内存
     */
    private Long swapMemoryUsed;
    /**
     * 总虚拟内存
     */
    private Long swapMemoryTotal;
    /**
     * 已使用内存
     */
    private Long memoryUsed;
    /**
     * 总内存
     */
    private Long memoryTotal;
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
    private Long diskUsed;
    /**
     * 总磁盘容量
     */
    private Long diskTotal;
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

    /**
     * 计算虚拟内存使用率
     */
    public void calcSwapMemoryUsage() {
        double usage = ((double) swapMemoryUsed / swapMemoryTotal) * 100;
        setSwapMemoryUsage(usage);
    }

    /**
     * 计算总内存率
     */
    public void calcMemoryUsage() {
        double usage = ((double) memoryUsed / memoryTotal) * 100;
        setMemoryUsage(usage);
    }

    /**
     * 计算磁盘使用率
     */
    public void calcDiskUsage() {
        double usage = ((double) diskUsed / diskTotal) * 100;
        setDiskUsage(usage);
    }
}
