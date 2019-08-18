package com.ddpzp.xiaogu_word.init;

import com.ddpzp.xiaogu_word.service.GameService;
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
    @Override
    public void run(String... args) throws Exception {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                initIdiom();
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
