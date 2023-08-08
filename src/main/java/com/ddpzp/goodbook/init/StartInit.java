package com.ddpzp.goodbook.init;

import com.ddpzp.goodbook.service.SpiderService;
import com.ddpzp.goodbook.service.SystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

/**
 * Created by dd
 * Date 2019/8/18 16:35
 */
@Component
@Slf4j
public class StartInit implements CommandLineRunner {
    @Autowired
    private SystemService systemService;
    @Autowired
    private SpiderService spiderService;

    @Override
    public void run(String... args) throws Exception {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                //初始化成语数据
                initIdiom();
                //初始化诗词数据
                initPoem();
                //清理超期的监控数据
                systemService.clearSystemInfoRecords();
            } catch (Exception e) {
                log.error("Init error!", e);
            }
        });
    }

    private void initIdiom() {
        log.info("Init idiom start...");
        spiderService.initIdiomData();
        log.info("Init idiom complete!");
    }

    private void initPoem(){
        log.info("Init poem start...");
        spiderService.initPoemData();
        log.info("Init poem complete!");
    }
}
