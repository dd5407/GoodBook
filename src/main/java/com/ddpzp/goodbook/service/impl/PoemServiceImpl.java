package com.ddpzp.goodbook.service.impl;

import com.ddpzp.goodbook.exception.GbException;
import com.ddpzp.goodbook.mapper.game.PoemMapper;
import com.ddpzp.goodbook.mapper.game.PoemTagMapper;
import com.ddpzp.goodbook.po.game.Poem;
import com.ddpzp.goodbook.po.game.PoemTag;
import com.ddpzp.goodbook.service.PoemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by dd
 * Date 2020/8/11 22:26
 */
@Service
@Slf4j
public class PoemServiceImpl implements PoemService {
    @Autowired
    private PoemMapper poemMapper;
    @Autowired
    private PoemTagMapper poemTagMapper;

    /**
     * 添加诗词
     *
     * @param poem 诗词
     * @param tags 标签
     */
    @Override
    @Transactional
    public void addPoem(Poem poem, Set<String> tags) throws GbException {
        String title = poem.getTitle();
        String author = poem.getAuthor();
        if (StringUtils.isBlank(title)) {
            throw new GbException("标题不能为空！");
        }
        if (StringUtils.isBlank(author)) {
            throw new GbException("作者不能为空");
        }
        //检查数据库中是否存在相同的数据，由于名称相同的诗词很常见，所以加上作者一起校验
        Poem poemInDb = poemMapper.checkRepeat(title, author);
        if (poemInDb != null) {
            throw new GbException(String.format("诗词[%s]-%s已存在！", title, author));
        }

        if (StringUtils.isBlank(poem.getBody())) {
            throw new GbException("诗词主体不能为空！");
        }

        poemMapper.addPoem(poem);
        for (String tag : tags) {
            PoemTag poemTag = new PoemTag();
            poemTag.setPoemId(poem.getId());
            poemTag.setTag(tag);
            poemTagMapper.addTag(poemTag);
        }
        log.info("诗词添加成功，名称：{}，作者：{}", title, author);
    }

    /**
     * 模糊查询诗词
     *
     * @param title
     * @param author
     * @param body
     * @param current
     * @param pageSize
     * @return
     * @throws GbException
     */
    @Override
    public List<Poem> fuzzySearchPoem(String title, String author, String body, Integer current, Integer pageSize) {
        Integer startNum = null;
        if (current != null && pageSize != null) {
            startNum = (current - 1) * pageSize;
        }
        return poemMapper.fuzzySearchPoem(title, author, body, startNum, pageSize);
    }

    /**
     * 模糊查询诗词，查询总数
     *
     * @param title
     * @param author
     * @param body
     * @return
     */
    @Override
    public Integer countFuzzySearchPoem(String title, String author, String body) {
        return poemMapper.countFuzzySearchPoem(title, author, body);
    }

    /**
     * 全匹配查询诗词
     *
     * @param title
     * @param author
     * @param current
     * @param pageSize
     * @return
     * @throws GbException
     */
    @Override
    public List<Poem> searchPoem(String title, String author, Integer current, Integer pageSize) {
        Integer startNum = null;
        if (current != null && pageSize != null) {
            startNum = (current - 1) * pageSize;
        }
        return poemMapper.searchPoem(title, author, startNum, pageSize);
    }

    /**
     * 全匹配查询诗词，查询总数
     *
     * @param title
     * @param author
     * @return
     */
    @Override
    public Integer countSearchPoem(String title, String author) {
        return poemMapper.countSearchPoem(title, author);
    }

}
