package com.ddpzp.goodbook.mapper.system;

import com.ddpzp.goodbook.po.system.SystemConfig;

/**
 * Created by dd
 * Date 2020/2/23 20:05
 */
public interface SystemConfigMapper {
    SystemConfig getConfig(String ip);

    void addConfig(SystemConfig config);
}
