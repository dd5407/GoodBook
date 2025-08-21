package com.ddpzp.goodbook.controller;

import com.ddpzp.goodbook.model.JsonResult;
import com.ddpzp.goodbook.model.system.SystemInfoModel;
import com.ddpzp.goodbook.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by dd
 * Date 2020/2/25 0:48
 */
@Slf4j
@Controller
@RequestMapping("/goodbook/system/")
public class SystemController extends BaseController {
    @Autowired
    private SystemService systemService;

    @GetMapping("page/monitor")
    public String monitorPage() {
        return "systemMonitor";
    }

    @GetMapping("latestInfo")
    @ResponseBody
    public JsonResult getLatestSystemInfoRecord() {
        try {
            SystemInfoModel latestSystemInfoRecord = systemService.getLatestSystemInfoRecord();
            return JsonResult.success(latestSystemInfoRecord);
        } catch (Exception e) {
            log.error("Get latest system info error!", e);
            return JsonResult.error(e.getMessage());
        }
    }

    @GetMapping("infos")
    @ResponseBody
    public JsonResult getSystemInfoRecords(Integer page, Integer pageSize) {
        if (page == null ^ pageSize == null) {
            return JsonResult.error("参数错误");
        }
        try {
            List<SystemInfoModel> records = systemService.getLocalSystemInfoRecords(page, pageSize);
            return JsonResult.success(records);
        } catch (Exception e) {
            log.error("Get system info records error!", e);
            return JsonResult.error(e.getMessage());
        }
    }

    @PostMapping("changeStatus")
    @ResponseBody
    public JsonResult changeMonitorStatus(@RequestParam Boolean openMonitor) {
        try {
            systemService.changeMonitorStatus(openMonitor);
            return JsonResult.success();
        } catch (Exception e) {
            log.error("Change system monitor status failed! request status:{}", openMonitor);
            return JsonResult.error(e.getMessage());
        }
    }
}
