package com.ddpzp.xiaogu_word.service;

import com.ddpzp.xiaogu_word.po.game.Frog;

import java.util.List;

/**
 * Created by dd
 * Date 2019/8/12 20:05
 */
public interface GameService {
    List<Frog> countFrog(Integer startNum, Integer size);
}
