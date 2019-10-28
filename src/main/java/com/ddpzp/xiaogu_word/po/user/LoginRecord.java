package com.ddpzp.xiaogu_word.po.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 用户登陆记录
 * Created by dd
 * Date 2019/10/29 1:59
 */
@Data
public class LoginRecord {
    /**
     * 自增主键
     */
    private Integer id;
    /**
     * 登陆账号
     */
    private String account;
    /**
     * 登陆ip
     */
    private String ip;
    /**
     * 登陆时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginTime;
}
