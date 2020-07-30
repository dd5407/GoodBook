package com.ddpzp.xiaogu_word.po.game;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * Created by dd
 * Date 2020/7/30 0:16
 */
@Data
public class Poem {
    private Integer id;
    /**
     * 诗名
     */
    private String title;
    /**
     * 作者
     */
    private String author;
    /**
     * 朝代
     */
    private String dynasty;
    /**
     * 诗主体
     */
    private String body;
    /**
     * 译文
     */
    private String means;
    /**
     * 作者介绍
     */
    private String authorDetail;
    /**
     * 作者介绍百度百科链接
     */
    private String authorBaikeLink;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
