package com.ddpzp.goodbook.po.system;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 系统监控配置
 * Created by dd
 * Date 2020/2/23 20:02
 */
@Data
public class SystemConfig {
    private Integer id;
    /**
     * 开关，关闭不收集日志
     */
    private Boolean enableSystemInfoCollection;
    private String ip;
    /**
     * 系统监控数据最大保存天数
     */
    private Integer systemInfoMaxSaveDay;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
