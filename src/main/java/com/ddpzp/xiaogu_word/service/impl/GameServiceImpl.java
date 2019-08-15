package com.ddpzp.xiaogu_word.service.impl;

import com.ddpzp.xiaogu_word.po.game.Frog;
import com.ddpzp.xiaogu_word.service.GameService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dd
 * Date 2019/8/12 20:06
 */
@Service
public class GameServiceImpl implements GameService {
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
}
