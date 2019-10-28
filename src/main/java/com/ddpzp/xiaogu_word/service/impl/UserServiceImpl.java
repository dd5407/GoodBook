package com.ddpzp.xiaogu_word.service.impl;

import com.ddpzp.xiaogu_word.mapper.user.UserMapper;
import com.ddpzp.xiaogu_word.po.user.LoginRecord;
import com.ddpzp.xiaogu_word.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dd
 * Date 2019/10/29 1:55
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 添加登录日志
     *
     * @param username 登录账号
     * @param ip       登录ip
     */
    @Override
    public void addLoginRecord(String username, String ip) {
        LoginRecord loginRecord = new LoginRecord();
        loginRecord.setAccount(username);
        loginRecord.setIp(ip);
        userMapper.addLoginRecord(loginRecord);
    }

    /**
     * 获取登录日志
     *
     * @param account 登录账号
     * @return
     */
    @Override
    public List<LoginRecord> getLoginRecords(String account) {
        return userMapper.getLoginRecords(account);
    }
}
