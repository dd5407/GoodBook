package com.ddpzp.goodbook.po.user;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * 用户
 * Created by dd
 * Date 2021/4/14 22:54
 */
@Data
@ToString
public class User {
    /**
     * 主键
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 注册时间
     */
    private Date createTime;
    /**
     * 密码更新时间
     */
    private Date updatePwdTime;
}
