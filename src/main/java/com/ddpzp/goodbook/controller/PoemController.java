package com.ddpzp.goodbook.controller;

import com.alibaba.fastjson.JSONObject;
import com.ddpzp.goodbook.model.JsonResult;
import com.ddpzp.goodbook.po.game.Poem;
import com.ddpzp.goodbook.service.PoemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 诗词
 * Created by dd
 * Date 2020/8/11 23:32
 */
@Slf4j
@Controller
@RequestMapping("/goodbook/poem/")
public class PoemController {
    @Autowired
    private PoemService poemService;

    @ResponseBody
    @RequestMapping("/fuzzySearch")
    public JsonResult fuzzySearch(String title, String author, String body, Integer current, Integer pageSize) {
        if (StringUtils.isBlank(title) && StringUtils.isBlank(author) && StringUtils.isBlank(body)) {
            return JsonResult.error("标题、作者和内容不能同时为空！");
        }
        if (current == null ^ pageSize == null) {
            return JsonResult.error("分页参数错误！");
        }
        try {
            List<Poem> poems = poemService.fuzzySearchPoem(title, author, body, current, pageSize);
            Integer total = poemService.countFuzzySearchPoem(title, author, body);
            JSONObject result = new JSONObject();
            result.put("data", poems);
            result.put("total", total);
            return JsonResult.success(result);
        } catch (Exception e) {
            log.error("诗词模糊查询失败！", e);
            return JsonResult.error(String.format("诗词模糊查询失败！%s", e.getMessage()));
        }
    }

    @ResponseBody
    @RequestMapping("/search")
    public JsonResult search(String title, String author, Integer current, Integer pageSize) {
        if (StringUtils.isBlank(title) && StringUtils.isBlank(author)) {
            return JsonResult.error("标题和作者不能同时为空！");
        }
        if (current == null ^ pageSize == null) {
            return JsonResult.error("分页参数错误！");
        }
        try {
            List<Poem> poems = poemService.searchPoem(title, author, current, pageSize);
            Integer total = poemService.countSearchPoem(title, author);
            JSONObject result = new JSONObject();
            result.put("data", poems);
            result.put("total", total);
            return JsonResult.success(result);
        } catch (Exception e) {
            log.error("诗词精确查询失败！", e);
            return JsonResult.error(String.format("诗词精确查询失败！%s", e.getMessage()));
        }
    }
}
