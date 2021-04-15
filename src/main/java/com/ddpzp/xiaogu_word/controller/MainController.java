package com.ddpzp.xiaogu_word.controller;

import com.ddpzp.xiaogu_word.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dd
 * Date 2019/7/26 21:51
 */
@Controller
@Slf4j
public class MainController {
    @Autowired
    private HttpServletRequest request;
    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "欢迎使用Good Book!");
        log.info("welcome! ip:[{}]", SystemUtil.getUserIp(request));
        return "home";
    }

    @RequestMapping("/head")
    public String head() {
        return "head";
    }
}
