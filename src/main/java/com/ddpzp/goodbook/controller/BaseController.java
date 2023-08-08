package com.ddpzp.goodbook.controller;

import com.ddpzp.goodbook.common.Constants;

import javax.servlet.http.HttpSession;

/**
 * Created by dd
 * Date 2019/8/13 10:51
 */
public class BaseController {
    public String getUsername(HttpSession session){
        return (String) session.getAttribute(Constants.SESSION_USERNAME_KEY);
    }
    public Integer getUserId(HttpSession session){
        return (Integer) session.getAttribute(Constants.SESSION_USERID_KEY);
    }
}
