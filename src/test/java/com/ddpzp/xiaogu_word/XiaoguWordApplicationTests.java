package com.ddpzp.xiaogu_word;

import com.alibaba.fastjson.JSON;
import com.ddpzp.xiaogu_word.mapper.word.WordMapper;
import com.ddpzp.xiaogu_word.po.Word;
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

    @Test
    public void getWordList() {
        List<Word> words = wordService.getWords("注入", "dd", 1, 10);
        System.out.println(JSON.toJSONString(words));
    }

    @Test
    public void contextLoads() {
    }

}
