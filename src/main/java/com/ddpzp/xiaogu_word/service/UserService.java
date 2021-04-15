package com.ddpzp.xiaogu_word.service;

import com.ddpzp.xiaogu_word.po.user.LoginRecord;
import com.ddpzp.xiaogu_word.po.user.User;

import java.util.List;

/**
 * Created by dd
 * Date 2019/10/29 1:55
 */
public interface UserService {
    /**
     * 添加登录日志
     *
     * @param username 登录账号
     * @param ip       登录ip
     */
    void addLoginRecord(String username, String ip);

    /**
     * 获取登录日志
     *
     * @param account 登录账号
     * @return
     */
    List<LoginRecord> getLoginRecords(String account);

    /**
     * 根据用户名获取用户
     *
     * @param username
     * @return
     */
    User getUser(String username);

    /**
     * 添加用户
     *
     * @param user
     */
    void addUser(User user);
}
