package com.ddpzp.xiaogu_word.service;

import com.ddpzp.xiaogu_word.po.game.Frog;
import com.ddpzp.xiaogu_word.po.game.Idiom;

import java.util.List;

/**
 * Created by dd
 * Date 2019/8/12 20:05
 */
public interface GameService {
    /**
     * 数青蛙
     *
     * @param startNum
     * @param size
     * @return
     */
    List<Frog> countFrog(Integer startNum, Integer size);

    /**
     * 随机获取一个成语
     *
     * @return
     */
    Idiom randomIdiom() throws Exception;
}
