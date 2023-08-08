package com.ddpzp.goodbook.service.impl;

import com.ddpzp.goodbook.mapper.word.WordMapper;
import com.ddpzp.goodbook.po.Word;
import com.ddpzp.goodbook.service.WordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by dd
 * Date 2019/7/25 0:24
 */
@Service
@Slf4j
public class WordServiceImpl implements WordService {
    @Autowired
    private WordMapper wordMapper;

    @Override
    public List<Word> getWords(String query, String username, Integer current, Integer pageSize) {
        log.info("Get words. query={},username={},current={},pageSize={}", query, username, current, pageSize);
        Integer startNum;
        if (current == null) {
            startNum = null;
        } else {
            startNum = (current - 1) * pageSize;
        }
        return wordMapper.getWords(query, username, startNum, pageSize);
    }

    @Override
    public void addWord(Word word) {
        wordMapper.addWord(word);
    }

    @Override
    public List<Word> getWord(Integer id) {
        return wordMapper.getWord(id);
    }

    @Override
    public void deleteWord(Integer id) {
        wordMapper.deleteWord(id);
    }

    @Override
    public Integer countWords(String query, String username) {
        return wordMapper.countWords(query, username);
    }

    @Override
    public void updateWord(Word word) {
        word.setUpdateTime(new Date());
        wordMapper.updateWord(word);
    }

    @Override
    public List<Word> getWordByEnglish(String english, String creator) {
        return wordMapper.getWordByEnglish(english, creator);
    }

    @Override
    public void batchDelete(Integer[] ids) {
        wordMapper.batchDelete(ids);
    }
}
