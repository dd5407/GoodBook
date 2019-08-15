package com.ddpzp.xiaogu_word.po.game;

import lombok.Data;

/**
 * 数青蛙游戏，青蛙
 * Created by dd
 * Date 2019/7/30 22:23
 */
@Data
public class Frog {
    private Integer num;
    private Integer month;
    private Integer eye;
    private Integer leg;

    public Frog(Integer num) {
        this.num = num;
        this.month = num;
        this.eye = num * 2;
        this.leg = num * 4;
    }
}
