package com.ddpzp.xiaogu_word.po.game;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 诗词标签，每首诗每个标签1条记录
 * Created by dd
 * Date 2020/7/30 0:32
 */
@Data
public class PoemTag {
    private Integer id;
    /**
     * 古诗id
     */
    private Integer poemId;
    /**
     * 标签
     */
    private String tag;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
