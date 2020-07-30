package com.ddpzp.xiaogu_word.mapper.game;

import com.ddpzp.xiaogu_word.po.game.Poem;
import org.apache.ibatis.annotations.Param;

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
}
