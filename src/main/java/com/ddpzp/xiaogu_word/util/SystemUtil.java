package com.ddpzp.xiaogu_word.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by dd
 * Date 2020/2/23 20:23
 */
@Slf4j
public class SystemUtil {
    /**
     * 获取本机（服务器）IP
     *
     * @return
     */
    public static String getLocalIp() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            String hostAddress = inetAddress.getHostAddress();
            return hostAddress;
        } catch (UnknownHostException e) {
            log.error("获取本机IP异常！", e);
            return null;
        }
    }
}
