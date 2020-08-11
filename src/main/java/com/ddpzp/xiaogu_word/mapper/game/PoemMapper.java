package com.ddpzp.xiaogu_word.mapper.game;

import com.ddpzp.xiaogu_word.po.game.Poem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 诗词mapper
 * Created by dd
 * Date 2020/7/30 3:09
 */
public interface PoemMapper {
    /**
     * 添加诗词
     *
     * @param poem
     */
    void addPoem(Poem poem);

    /**
     * 根据诗词名和作者进行重复检查
     *
     * @param title
     * @param author
     * @return
     */
    Poem checkRepeat(@Param("title") String title, @Param("author") String author);

    /**
     * 查询诗词总数
     *
     * @return
     */
    Integer countPoem();

    /**
     * 模糊查询诗词
     *
     * @param title
     * @param author
     * @param body
     * @param startNum
     * @param pageSize
     * @return
     */
    List<Poem> fuzzySearchPoem(@Param("title") String title, @Param("author") String author,
                               @Param("body") String body, @Param("startNum") Integer startNum,
                               @Param("pageSize") Integer pageSize);

    /**
     * 模糊查询诗词，查询总数
     *
     * @param title
     * @param author
     * @param body
     * @return
     */
    Integer countFuzzySearchPoem(@Param("title") String title, @Param("author") String author,
                                 @Param("body") String body);

    /**
     * 获取诗词，全匹配
     *
     * @param title
     * @param author
     * @param startNum
     * @param pageSize
     * @return
     */
    List<Poem> searchPoem(@Param("title") String title, @Param("author") String author,
                          @Param("startNum") Integer startNum, @Param("pageSize") Integer pageSize);

    /**
     * 全匹配查询诗词，查询总数
     *
     * @param title
     * @param author
     * @return
     */
    Integer countSearchPoem(@Param("title") String title, @Param("author") String author);
}
