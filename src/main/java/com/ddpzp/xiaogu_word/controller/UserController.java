package com.ddpzp.xiaogu_word.controller;

import com.alibaba.fastjson.JSON;
import com.ddpzp.xiaogu_word.common.Constants;
import com.ddpzp.xiaogu_word.common.ErrorCode;
import com.ddpzp.xiaogu_word.model.JsonResult;
import com.ddpzp.xiaogu_word.po.user.LoginRecord;
import com.ddpzp.xiaogu_word.po.user.User;
import com.ddpzp.xiaogu_word.service.UserService;
import com.ddpzp.xiaogu_word.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
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
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;

    @PostMapping("login")
    @ResponseBody
    public JsonResult login(String username, String password) {
        try {
            if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
                log.warn("Login failed! username or password is blank!");
                return JsonResult.error("用户名或密码为空！");
            }
            User user = userService.getUser(username);
            if (user == null) {
                log.warn("User {} not exists!", username);
                return JsonResult.error("用户不存在！");
            } else if (!StringUtils.equals(user.getPassword(), DigestUtils.md5DigestAsHex(password.getBytes()))) {
                return JsonResult.error("密码错误！");
            }
            log.info("login user:[{}]", username);
            HttpSession session = request.getSession();
            //登录后设置session，放入当前用户名，时效30分钟
            session.setAttribute(Constants.SESSION_USER_KEY, username);
            session.setMaxInactiveInterval(60 * 30);

            /* 记录登录日志 */
            String ip = null;
            try {
                ip = SystemUtil.getUserIp(request);
                userService.addLoginRecord(username, ip);
            } catch (Exception e) {
                log.error("Add login record failed! username={},ip={}", username, ip, e);
            }
            return JsonResult.success();
        } catch (Exception e) {
            log.error("login error!", e);
        }
        return JsonResult.error("系统错误，登陆失败！");
    }

    @GetMapping("logout")
    public String logout() {
        HttpSession session = request.getSession();
        String username = getUsername(session);
        //销毁session，并重定向回首页
        session.invalidate();
        log.info("Session invalidate success! logout user:[{}].", username);
        return "redirect:/";
    }

    @PostMapping("regist")
    @ResponseBody
    public JsonResult register(String username, String password) {
        try {
            if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
                return JsonResult.error("用户名或密码为空!");
            }
            User user = userService.getUser(username);
            if (user != null) {
                log.warn("用户{}已注册！");
                return JsonResult.error("用户已注册！");
            }

            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
            userService.addUser(newUser);
            return JsonResult.success();
        } catch (Exception e) {
            log.error("Regist failed! username={}", username, e);
            return JsonResult.error("系统错误，注册失败！");
        }
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
