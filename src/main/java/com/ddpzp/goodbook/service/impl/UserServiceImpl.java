package com.ddpzp.goodbook.service.impl;

import com.ddpzp.goodbook.mapper.user.LoginRecordMapper;
import com.ddpzp.goodbook.mapper.user.UserMapper;
import com.ddpzp.goodbook.po.user.LoginRecord;
import com.ddpzp.goodbook.po.user.User;
import com.ddpzp.goodbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by dd
 * Date 2019/10/29 1:55
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoginRecordMapper loginRecordMapper;

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
        loginRecordMapper.addLoginRecord(loginRecord);
    }

    /**
     * 获取登录日志
     *
     * @param account 登录账号
     * @return
     */
    @Override
    public List<LoginRecord> getLoginRecords(String account) {
        return loginRecordMapper.getLoginRecords(account);
    }

    /**
     * 根据用户名获取用户
     *
     * @param username
     * @return
     */
    @Override
    public User getUserByName(String username) {
        return userMapper.getUserByName(username);
    }

    @Override
    public User getUser(Integer id) {
        return userMapper.getUser(id);
    }

    /**
     * 添加用户
     *
     * @param user
     */
    @Override
    public void addUser(User user) {
        user.setCreateTime(new Date());
        user.setUpdatePwdTime(new Date());
        userMapper.addUser(user);
    }
}
