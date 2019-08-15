package com.ddpzp.xiaogu_word.controller;

import com.ddpzp.xiaogu_word.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by dd
 * Date 2019/7/27 0:45
 */
@RequestMapping("/gu/user/")
@Controller
@Slf4j
public class UserController extends BaseController{

    @PostMapping("login")
    public String login(String username, HttpServletRequest request) {
        log.info("login user:[{}]", username);
        HttpSession session = request.getSession();
        //登录后设置session，放入当前用户名，时效30分钟
        session.setAttribute(Constants.SESSION_USER_KEY, username);
        session.setMaxInactiveInterval(60 * 30);
        return "head";
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String username = getUsername(session);
        //销毁session，并重定向回首页
        session.invalidate();
        log.info("Session invalidate success! logout user:[{}].", username);
        return "redirect:/";
    }
}
