package com.ddpzp.xiaogu_word.mapper.game;

import com.ddpzp.xiaogu_word.po.game.GuessIdiom;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by dd
 * Date 2019/8/26 0:08
 */
public interface GuessIdiomMapper {
    /**
     * 猜成语-发送
     *
     * @param word
     * @param means
     * @param fromUsername
     * @param toUsername
     */
    void sendIdiom(@Param("word") String word, @Param("means") String means,
                   @Param("fromUsername") String fromUsername, @Param("toUsername") String toUsername);

    /**
     * 获取猜成语记录
     *
     * @param id
     * @return
     */
    GuessIdiom getGuessIdiom(Integer id);

    /**
     * 更新状态
     *
     * @param id
     * @param status 状态，0-未猜，1-猜中，2-未猜中
     */
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    /**
     * 获取猜成语列表
     *
     * @param startNum
     * @param pageSize
     * @param username
     * @return
     */
    List<GuessIdiom> getGuessIdiomList(@Param("startNum") Integer startNum,
                                       @Param("pageSize") Integer pageSize,
                                       @Param("username") String username);

    /**
     * 获取猜成语记录总数
     *
     * @param username
     * @return
     */
    Integer countGuessIdiom(@Param("username") String username);

    /**
     * 删除猜成语题目
     *
     * @param id
     */
    void deleteGuessIdiom(Integer id);

}
