package com.ddpzp.goodbook.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ddpzp.goodbook.common.Constants;
import com.ddpzp.goodbook.model.JsonResult;
import com.ddpzp.goodbook.model.health.WeightModel;
import com.ddpzp.goodbook.po.health.Weight;
import com.ddpzp.goodbook.service.HealthService;
import com.ddpzp.goodbook.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/gu/health/")
public class HealthController extends BaseController{
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpSession session;
    @Autowired
    private HealthService healthService;

    /**
     * 跳转体重页面
     *
     * @return
     */
    @GetMapping("/page/health/weight")
    public String weightPage(Model model) {
        try {
            Integer userId = getUserId(session);
            log.info("Get weight records,username={}", getUsername(session));
            List<WeightModel> weightRecords = healthService.getWeightRecordsByUser(userId, null, null);
            Integer total = healthService.getWeightTotalsByUser(userId);
            model.addAttribute("weightItems", weightRecords);
            model.addAttribute("current", 1);
            model.addAttribute("total", total);
        } catch (Exception e) {
            log.error("获取体重列表失败！", e);
            model.addAttribute(Constants.ERROR_MSG_KEY, String.format("获取体重列表失败,错误信息：[%s]", e.getMessage()));
        }
        return "weight";
    }

    @PostMapping("/weight/add")
    @ResponseBody
    public JsonResult addWeightRecord(@RequestBody Weight weight) {
        if (ObjectUtil.isAllFieldEmpty(weight)) {
            log.warn("所有属性为空，必须填写一项！");
            return JsonResult.error("必须填写一项！");
        }
        try {
            weight.setUserId(getUserId(session));
            healthService.add(weight);
            log.info("体重记录添加成功。{}", JSON.toJSONString(weight));
            return JsonResult.success();
        } catch (Exception e) {
            log.error("体重记录添加失败！", e);
            return JsonResult.error("体重记录添加失败！" + e.getMessage());
        }
    }

    @GetMapping("/weight/getRecordByUser")
    @ResponseBody
    public JsonResult getRecordByUser(@RequestParam(required = false) Integer current,
                                      @RequestParam(required = false) Integer pageSize) {
        try {
            Integer userId = getUserId(session);
            List<WeightModel> records = healthService.getWeightRecordsByUser(userId, current, pageSize);
            Integer total = healthService.getWeightTotalsByUser(userId);
            JSONObject obj = new JSONObject();
            obj.put("data", records);
            obj.put("total", total);
            return JsonResult.success(obj);
        } catch (Exception e) {
            log.error("获取体重记录列表失败！", e);
            return JsonResult.error("获取体重记录列表失败！" + e.getMessage());
        }
    }

    @PostMapping("/weight/delete")
    @ResponseBody
    public JsonResult deleteRecord(Integer id) {
        try {
            healthService.deleteWeightRecord(id);
            return JsonResult.success();
        } catch (Exception e) {
            log.error("体重记录删除失败！", e);
            return JsonResult.error("体重记录删除失败！" + e.getMessage());
        }
    }

    @PostMapping("/weight/batchDelete")
    @ResponseBody
    public JsonResult deleteRecord(Integer[] ids) {
        try {
            healthService.deleteWeightRecords(ids);
            return JsonResult.success();
        } catch (Exception e) {
            log.error("体重记录删除失败！", e);
            return JsonResult.error("体重记录删除失败！" + e.getMessage());
        }
    }
}
