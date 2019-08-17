package com.ddpzp.xiaogu_word.service.impl;

import com.ddpzp.xiaogu_word.mapper.game.IdiomMapper;
import com.ddpzp.xiaogu_word.po.game.Frog;
import com.ddpzp.xiaogu_word.po.game.Idiom;
import com.ddpzp.xiaogu_word.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dd
 * Date 2019/8/12 20:06
 */
@Service
@Slf4j
public class GameServiceImpl implements GameService {
    @Autowired
    private IdiomMapper idiomMapper;

    @Override
    public List<Frog> countFrog(Integer startNum, Integer size) {
        List<Frog> list = new ArrayList<>();
        if (startNum == null || size == null) {
            return list;
        }
        for (int i = startNum; i < startNum + size; i++) {
            list.add(new Frog(i));
        }
        return list;
    }

    @Override
    public Idiom randomIdiom() throws Exception {
        Integer total = idiomMapper.countIdiom();
        if (total <= 0) {
            throw new Exception("数据库中没有成语！");
        }
        //生成一个0到total-1的随机数
        int randomIndex = (int) (Math.random() * (total));
        log.info("生成[0-{}]随机数：[{}]", total, randomIndex);
        return idiomMapper.getIdiomByIndex(randomIndex);
    }
}
