package com.ddpzp.xiaogu_word.init;

import com.ddpzp.xiaogu_word.service.GameService;
import com.ddpzp.xiaogu_word.service.SystemService;
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
    private GameService gameService;
    @Autowired
    private SystemService systemService;

    @Override
    public void run(String... args) throws Exception {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                //初始化成语数据
                initIdiom();
                //清理超期的监控数据
                systemService.clearSystemInfoRecords();
            } catch (Exception e) {
                log.error("Init error!", e);
            }
        });
    }

    private void initIdiom() {
        log.info("Init idioms start...");
        gameService.initIdiomData();
        log.info("Init idioms complete!");
    }
}
