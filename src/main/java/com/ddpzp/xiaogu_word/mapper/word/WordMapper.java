package com.ddpzp.xiaogu_word.mapper.word;

import com.ddpzp.xiaogu_word.po.Word;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by dd
 * Date 2019/7/25 0:23
 */

public interface WordMapper {
    List<Word> getWords(@Param("query") String query, @Param("username") String username,
                        @Param("startNum") Integer startNum, @Param("pageSize") Integer pageSize);

    void addWord(Word word);

    List<Word> getWord(Integer id);

    void deleteWord(Integer id);

    Integer countWords(@Param("query") String query, @Param("username") String username);

    void updateWord(Word word);

    List<Word> getWordByEnglish(@Param("english") String english,
                               @Param("creator") String creator);

    void batchDelete(@Param("ids") Integer[] ids);
}
