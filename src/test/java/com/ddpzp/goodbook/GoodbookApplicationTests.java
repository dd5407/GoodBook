package com.ddpzp.goodbook;

import com.alibaba.fastjson.JSON;
import com.ddpzp.goodbook.exception.GbException;
import com.ddpzp.goodbook.mapper.word.WordMapper;
import com.ddpzp.goodbook.po.Word;
import com.ddpzp.goodbook.po.game.Idiom;
import com.ddpzp.goodbook.service.GameService;
import com.ddpzp.goodbook.service.WordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodbookApplicationTests {
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
    public void idiomLoongTest() {
        Idiom idiom = null;
        try {
            idiom = gameService.idiomLoong("目不转睛", 4);
            System.out.println(idiom.getWord());
        } catch (GbException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void contextLoads() {
    }

}
