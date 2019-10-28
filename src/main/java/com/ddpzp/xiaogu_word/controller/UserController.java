package com.ddpzp.xiaogu_word.controller;

import com.ddpzp.xiaogu_word.common.Constants;
import com.ddpzp.xiaogu_word.model.JsonResult;
import com.ddpzp.xiaogu_word.po.user.LoginRecord;
import com.ddpzp.xiaogu_word.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by dd
 * Date 2019/7/27 0:45
 */
@RequestMapping("/gu/user/")
@Controller
@Slf4j
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @PostMapping("login")
    public String login(String username, HttpServletRequest request) {
        if (StringUtils.isBlank(username)) {
            log.warn("Login failed! username is blank!");
            return "home";
        }
        log.info("login user:[{}]", username);
        HttpSession session = request.getSession();
        //登录后设置session，放入当前用户名，时效30分钟
        session.setAttribute(Constants.SESSION_USER_KEY, username);
        session.setMaxInactiveInterval(60 * 30);

        /* 记录登录日志 */
        String ip = null;
        try {
            ip = request.getRemoteAddr();
            userService.addLoginRecord(username, ip);
        } catch (Exception e) {
            log.error("Add login record failed! username={},ip={}", username, ip);
        }
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

    /**
     * 获取用户登录日志
     *
     * @param account
     * @return
     */
    @GetMapping("loginRecords")
    @ResponseBody
    public JsonResult getLoginRecords(String account) {
        if (StringUtils.equals("全部", account)) {
            account = null;
        } else if (StringUtils.isBlank(account)) {
            return JsonResult.error("用户名必填！");
        }
        try {
            List<LoginRecord> recordList = userService.getLoginRecords(account);
            return JsonResult.success(recordList);
        } catch (Exception e) {
            log.error("Get login records failed!", e);
            return JsonResult.error("系统错误！" + e.getMessage());
        }
    }
}
