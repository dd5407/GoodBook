package com.ddpzp.xiaogu_word.controller;

import com.alibaba.fastjson.JSONObject;
import com.ddpzp.xiaogu_word.model.JsonResult;
import com.ddpzp.xiaogu_word.model.social.SlimRecordModel;
import com.ddpzp.xiaogu_word.service.SlimService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dd
 * Date 2021/4/6 21:31
 */
@Controller
@Slf4j
@RequestMapping("/gu/slim/")
public class SlimController extends BaseController {
    @Autowired
    private SlimService slimService;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpSession session;

    /**
     * 跳转瘦身页面
     *
     * @return
     */
    @GetMapping("/page/slim")
    public String slimPage() {
        return "slim";
    }


    @GetMapping("records")
    @ResponseBody
    public JsonResult slimRecord(@RequestParam(required = false, defaultValue = "1") Integer current,
                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        try {
            String username = getUsername(session);
            List<SlimRecordModel> records = slimService.getRecords(username, current, pageSize);
            Integer total = slimService.getTatol(username);
            JSONObject obj = new JSONObject();
            obj.put("data", records);
            obj.put("total", total);
            return JsonResult.success(obj);
        } catch (Exception e) {
            log.error("获取瘦身记录失败！", e);
            return JsonResult.error(String.format("获取瘦身记录失败，错误信息：[%s]", e.getMessage()));
        }
    }

    @PostMapping("addRecord")
    @ResponseBody
    public JsonResult addRecord(@RequestBody SlimRecordModel model) {
        try {
            if (hasTodayRecord()) {
                log.warn("今天的记录已存在！");
                return JsonResult.error("今天的记录已存在！");
            }

            return JsonResult.success();
        } catch (Exception e) {
            log.error("添加瘦身记录失败！", e);
            return JsonResult.error(String.format("添加瘦身记录失败，错误信息：[%s]", e.getMessage()));
        }
    }

    private boolean hasTodayRecord() {
        List<SlimRecordModel> records = slimService.getRecordByDate(LocalDate.now(), getUsername(session));
        return !records.isEmpty();
    }

}
