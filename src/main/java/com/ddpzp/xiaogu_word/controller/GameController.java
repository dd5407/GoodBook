package com.ddpzp.xiaogu_word.controller;

import com.ddpzp.xiaogu_word.common.Constants;
import com.ddpzp.xiaogu_word.model.JsonResult;
import com.ddpzp.xiaogu_word.po.game.Frog;
import com.ddpzp.xiaogu_word.po.game.Idiom;
import com.ddpzp.xiaogu_word.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by dd
 * Date 2019/7/30 22:13
 */
@Slf4j
@RequestMapping("/gu/game/")
@Controller
public class GameController extends BaseController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpSession session;
    @Autowired
    private GameService gameService;

    /**
     * 数青蛙
     *
     * @param startNum 从第几只开始数
     * @param size     数几只
     * @return
     */
    @GetMapping(value = "frogCount")
    @ResponseBody
    public JsonResult frogCount(@RequestParam(required = false, defaultValue = "1") Integer startNum,
                                @RequestParam(required = false, defaultValue = "100") Integer size,
                                @RequestParam(required = false) Boolean crazyMode) {
        if (startNum <= 0 || size < 0) {
            return JsonResult.error("参数错误！");
        }
        if (startNum + size > 10000 && !crazyMode) {
            return JsonResult.error("大佬，你数不完的！");
        }
        try {
            List<Frog> list = gameService.countFrog(startNum, size);
            log.info("Count frog success! startNum:[{}],size:[{}],user:[{}].",
                    startNum, size, getUsername(session));
            return JsonResult.success(list);
        } catch (Exception e) {
            log.error("Count frog failed!", e);
            return JsonResult.error(String.format("数青蛙失败！%s", e.getMessage()));
        }
    }

    /**
     * 跳转数青蛙页面
     *
     * @param model
     * @return
     */
    @GetMapping("/countFrogPage")
    public String frogCountPage(Model model) {
        String username = getUsername(session);
        log.info("Count frog,username={}", username);
        List<Frog> list = gameService.countFrog(1, 100);
        model.addAttribute("frogList", list);
        return "countFrog";
    }

    @GetMapping("/randomIdiom")
    @ResponseBody
    public JsonResult randomIdiom() {
        try {
            Idiom idiom = gameService.randomIdiom();
            return JsonResult.success(idiom);
        } catch (Exception e) {
            log.error("随机获取成语失败！", e);
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 跳转成语页面
     *
     * @return
     */
    @GetMapping("/idiomPage")
    public String idiomPage() {
        String username = getUsername(session);
        log.info("Idiom,username={}", username);
        return "idiom";
    }
}
