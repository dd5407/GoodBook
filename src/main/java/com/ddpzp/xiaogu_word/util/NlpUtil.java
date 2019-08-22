package com.ddpzp.xiaogu_word.util;

import com.ddpzp.xiaogu_word.po.game.Idiom;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.py.Pinyin;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by dd
 * Date 2019/8/19 0:04
 */
public class NlpUtil {

    public static void setIdiomPinyin(Idiom idiom) throws Exception {
        if (idiom == null || StringUtils.isBlank(idiom.getWord())) {
            throw new Exception("成语为空");
        }
        List<Pinyin> pinyins = HanLP.convertToPinyinList(idiom.getWord());
        idiom.setFirstPinyin(pinyins.get(0).getPinyinWithoutTone());
        idiom.setSecondPinyin(pinyins.get(1).getPinyinWithoutTone());
        idiom.setThirdPinyin(pinyins.get(2).getPinyinWithoutTone());
        idiom.setFourthPinyin(pinyins.get(3).getPinyinWithoutTone());
    }

    public static boolean isPinyin(String word) {
        if (StringUtils.isEmpty(word)) {
            return false;
        }
        return word.matches("^[a-zA-Z]+$");
    }

    public static boolean isFourIdiom(String word) {
        if (StringUtils.isEmpty(word)) {
            return false;
        }
        return word.matches("[\\u4e00-\\u9fa5]{4}");
    }

    public static boolean isChinese(String word) {
        if (StringUtils.isEmpty(word)) {
            return false;
        }
        return word.matches("[\\u4e00-\\u9fa5]+");
    }
}
