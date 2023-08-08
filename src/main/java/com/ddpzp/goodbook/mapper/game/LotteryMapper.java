package com.ddpzp.goodbook.mapper.game;

import com.ddpzp.goodbook.po.game.LotteryItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 抽奖mapper
 * Created by dd
 * Date 2020/2/19 2:40
 */
public interface LotteryMapper {
    List<LotteryItem> getLotteryItems(@Param("creator") String creator,
                                      @Param("startNum") Integer startNum,
                                      @Param("pageSize") Integer pageSize);

    Integer countLotteryItems(@Param("creator") String creator);

    List<LotteryItem> getLotteryItemByName(@Param("name") String name, @Param("creator") String creator);

    void addLotteryItem(LotteryItem lotteryItem);

    LotteryItem getLotteryItem(Integer id);

    void updateLotteryItem(LotteryItem lotteryItem);

    void deleteLotteryItem(Integer id);

    void batchDeleteLottery(@Param("ids") Integer[] ids);

    LotteryItem getLotteryItemByIndex(@Param("creator") String creator,@Param("index") Integer index);
}
