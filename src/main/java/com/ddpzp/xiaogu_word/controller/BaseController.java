package com.ddpzp.xiaogu_word.controller;

import com.ddpzp.xiaogu_word.common.Constants;

import javax.servlet.http.HttpSession;

/**
 * Created by dd
 * Date 2019/8/13 10:51
 */
public class BaseController {
    public String getUsername(HttpSession session){
        return (String) session.getAttribute(Constants.SESSION_USER_KEY);
    }
}
