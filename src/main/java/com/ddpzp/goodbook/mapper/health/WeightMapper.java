package com.ddpzp.goodbook.mapper.health;

import com.ddpzp.goodbook.po.health.Weight;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WeightMapper {
    /**
     * 添加体重数据
     *
     * @param weight
     */
    void addRecord(Weight weight);

    /**
     * 更新体重数据
     *
     * @param weight
     */
    void updateRecord(Weight weight);

    /**
     * 获取指定用户id的体重记录
     *
     * @param userId
     * @param startNum
     * @param pageSize
     * @return
     */
    List<Weight> getAllByUserId(@Param("userId") Integer userId,
                                @Param("startNum") Integer startNum, @Param("pageSize") Integer pageSize, @Param("timeRange") String timeRange);

    /**
     * 获取所有体重记录
     *
     * @param startNum
     * @param pageSize
     * @return
     */
    List<Weight> getAll(@Param("startNum") Integer startNum, @Param("pageSize") Integer pageSize);

    /**
     * 删除单条记录
     *
     * @param id
     */
    void delete(Integer id);

    Integer getTotalByUserId(Integer userId);

    void batchDelete(@Param("ids") Integer[] ids);
}
