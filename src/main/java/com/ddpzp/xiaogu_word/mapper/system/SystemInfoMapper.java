package com.ddpzp.xiaogu_word.mapper.system;

import com.ddpzp.xiaogu_word.po.system.SystemInformation;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by dd
 * Date 2020/2/23 20:05
 */
public interface SystemInfoMapper {
    /**
     * 添加监控记录
     *
     * @param systemInformation
     */
    void addSystemInfo(SystemInformation systemInformation);

    /**
     * 根据ip获取监控记录，ip为空，则获取所有
     *
     * @param ip
     * @param startNum
     * @param pageSize
     * @return
     */
    List<SystemInformation> getSystemInfoRecords(@Param("ip") String ip,
                                                 @Param("startNum") Integer startNum,
                                                 @Param("pageSize") Integer pageSize);

    /**
     * 根据ip获取最近一次监控数据
     *
     * @param ip ip不能为空
     * @return
     */
    SystemInformation getLatestSystemInfoRecord(String ip);

    /**
     * 删除某天前的监控数据
     *
     * @param ip
     * @param beforeDate
     */
    void deleteRecordBeforeDate(@Param("ip") String ip, @Param("beforeDate") Date beforeDate);
}
