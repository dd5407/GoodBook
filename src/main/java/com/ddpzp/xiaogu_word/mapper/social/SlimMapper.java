package com.ddpzp.xiaogu_word.mapper.social;

import com.ddpzp.xiaogu_word.po.social.SlimRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by dd
 * Date 2021/4/6 22:44
 */
public interface SlimMapper {
    /**
     * 分页获取瘦身记录
     *
     * @param username
     * @param startNum
     * @param pageSize
     * @return
     */
    List<SlimRecord> getRecords(@Param("username") String username, @Param("startNum") Integer startNum,
                                @Param("pageSize") Integer pageSize);

    /**
     * 获取瘦身记录数
     *
     * @param username
     * @return
     */
    Integer getTotal(@Param("username") String username);

    /**
     * 根据日期获取瘦身记录
     *
     * @param queryDate
     * @param username
     * @return
     */
    List<SlimRecord> getRecordsByDate(@Param("queryDate") String queryDate, String username);
}
