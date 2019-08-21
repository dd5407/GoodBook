package com.ddpzp.xiaogu_word;

import com.alibaba.fastjson.JSON;
import com.ddpzp.xiaogu_word.exception.GbException;
import com.ddpzp.xiaogu_word.mapper.word.WordMapper;
import com.ddpzp.xiaogu_word.po.Word;
import com.ddpzp.xiaogu_word.po.game.Idiom;
import com.ddpzp.xiaogu_word.service.GameService;
import com.ddpzp.xiaogu_word.service.WordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XiaoguWordApplicationTests {
    @Autowired
    private WordMapper wordMapper;
    @Autowired
    private WordService wordService;
    @Autowired
    private GameService gameService;

    @Test
    public void getWordList() {
        List<Word> words = wordService.getWords("注入", "dd", 1, 10);
        System.out.println(JSON.toJSONString(words));
    }

    @Test
    public void getRandomIdiom() throws Exception {
        try {
            for (int i = 0; i < 10; i++) {
                Idiom idiom = gameService.randomIdiom();
                System.out.println(JSON.toJSONString(idiom));
            }
        } catch (GbException ge) {
            System.out.println(ge.getMessage());
        }
    }

    @Test
    public void contextLoads() {
    }

}
