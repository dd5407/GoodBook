package com.ddpzp.xiaogu_word.service;

import com.ddpzp.xiaogu_word.po.Word;

import java.util.List;

/**
 * Created by dd
 * Date 2019/7/25 0:24
 */
public interface WordService {
    List<Word> getWords(String query, String username, Integer current, Integer pageSize);

    void addWord(Word word);

    List<Word> getWord(Integer id);

    void deleteWord(Integer id);

    Integer countWords(String query, String username);

    void updateWord(Word word);

    List<Word> getWordByEnglish(String english, String creator);

    void batchDelete(Integer[] ids);
}
