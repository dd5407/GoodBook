package com.ddpzp.xiaogu_word.mapper.system;

import com.ddpzp.xiaogu_word.po.system.SystemConfig;

/**
 * Created by dd
 * Date 2020/2/23 20:05
 */
public interface SystemConfigMapper {
    SystemConfig getConfig(String ip);
}
