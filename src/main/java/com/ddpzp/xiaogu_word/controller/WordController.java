package com.ddpzp.xiaogu_word.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ddpzp.xiaogu_word.common.Constants;
import com.ddpzp.xiaogu_word.model.JsonResult;
import com.ddpzp.xiaogu_word.po.Word;
import com.ddpzp.xiaogu_word.service.WordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by dd
 * Date 2018/10/23 0:44
 */
@Controller
@Slf4j
@RequestMapping("/gu/word/")
public class WordController extends BaseController {
    @Autowired
    private WordService wordService;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpSession session;

    @GetMapping(value = "page/list")
    public String listPage(Model model) {
        try {
            String username = getUsername(session);
            log.info("Get word list,username={}", username);
//            int current = 1;
//            int pageSize = 10;
//            List<Word> words = wordService.getWords(null, username, current, pageSize);
            List<Word> words = wordService.getWords(null, username, null, null);
            Integer total = wordService.countWords(null, username);
            model.addAttribute("words", words);
            model.addAttribute("current", 1);
            model.addAttribute("total", total);
        } catch (Exception e) {
            log.error("获取单词列表失败！", e);
            model.addAttribute(Constants.ERROR_MSG_KEY, String.format("获取单词列表失败,错误信息：[%s]", e.getMessage()));
        }
        return "wordlist";
    }

    @GetMapping(value = "wordList")
    @ResponseBody
    public JsonResult wordList(String query, @RequestParam(required = false, defaultValue = "1") Integer current,
                               @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        try {
            String username = getUsername(session);
            current = null;
            pageSize = null;
            List<Word> words = wordService.getWords(query, username, current, pageSize);
            Integer total = wordService.countWords(query, username);
            JSONObject obj = new JSONObject();
            obj.put("data", words);
            obj.put("total", total);
            return JsonResult.success(obj);
        } catch (Exception e) {
            log.error("获取单词列表失败！", e);
            return JsonResult.error(String.format("获取单词列表失败！错误信息：[%s]", e.getMessage()));
        }
    }

    @PostMapping(value = "addWord")
    @ResponseBody
    public JsonResult addWord(@RequestBody Word word) {
        if (StringUtils.isBlank(word.getChinese())) {
            return JsonResult.error("请填写中文！");
        }
        String english = word.getEnglish();
        if (StringUtils.isBlank(english)) {
            return JsonResult.error("请填写英文！");
        }
        word.setCreateTime(new Date());
        word.setCreator(getUsername(session));

        if (wordExists(english)) {
            log.warn("单词[{}]已存在！", english);
            return JsonResult.error(String.format("单词[%s]已存在！", english));
        }
        try {
            wordService.addWord(word);
            return JsonResult.success();
        } catch (Exception e) {
            log.error("添加单词失败！", e);
            return JsonResult.error(String.format("添加单词失败！错误信息：[%s]", e.getMessage()));
        }
    }

    @PostMapping(value = "updateWord")
    @ResponseBody
    public JsonResult updateWord(@RequestBody Word word) {
        String chinese = word.getChinese();
        String english = word.getEnglish();

        if (StringUtils.isBlank(chinese)) {
            return JsonResult.error("请填写中文！");
        }
        if (StringUtils.isBlank(english)) {
            return JsonResult.error("请填写英文！");
        }

        Integer id = word.getId();
        String wordJsonFromRequest = JSON.toJSONString(word);
        if (id == null) {
            log.error("Update word error, id is null! {}", wordJsonFromRequest);
            return JsonResult.error("id不存在，请刷新列表后再试！");
        }
        try {
            List<Word> wordInDatabaseList = wordService.getWord(id);
            if (wordInDatabaseList.size() < 1) {
                log.error("Update word error,word is not exists! Request word:[{}]", wordJsonFromRequest);
                return JsonResult.error("该单词记录不存在！请刷新列表后再试！");
            }

            Word wordInDatabase = wordInDatabaseList.get(0);
            boolean englishChanged = !StringUtils.equalsIgnoreCase(wordInDatabase.getEnglish(), english);
            if (englishChanged && wordExists(english)) {
                log.warn("单词[{}]已存在！", english);
                return JsonResult.error(String.format("单词[%s]已存在！", english));
            }

            //将修改存入数据库
            wordService.updateWord(word);
            return JsonResult.success();
        } catch (Exception e) {
            log.error("修改单词失败！", e);
            return JsonResult.error(String.format("修改单词失败，错误信息：[%s]", e.getMessage()));
        }
    }

    @PostMapping(value = "deleteWord")
    @ResponseBody
    public JsonResult deleteWord(Integer id) {
        try {
            if (id == null) {
                log.error("id不能为空");
                return JsonResult.error("id不能为空！");
            }
            List<Word> words = wordService.getWord(id);
            if (words.size() < 1) {
                log.error("单词不存在，id=[{}]", id);
                return JsonResult.error("单词不存在！");
            }
            Word word = words.get(0);
            wordService.deleteWord(id);
            log.info("用户[{}]删除单词[{}]成功！", getUsername(session), word.getEnglish());
            return JsonResult.success();
        } catch (Exception e) {
            log.error("删除单词失败！", e);
            return JsonResult.error(String.format("删除单词失败！错误信息：[%s]", e.getMessage()));
        }
    }

    @PostMapping("/batchDelete")
    @ResponseBody
    public JsonResult batchDelete(Integer[] ids) {
        if (ids == null || ids.length <= 0) {
            return JsonResult.error("至少选择一项！");
        }
        try {
            wordService.batchDelete(ids);
            log.info("批量删除成功！id:[{}]", Arrays.toString(ids));
            return JsonResult.success();
        } catch (Exception e) {
            log.error("批量删除单词失败！", e);
            return JsonResult.error(String.format("批量删除单词失败！错误信息：[%s]", e.getMessage()));
        }
    }

    private boolean wordExists(String english) {
        List<Word> words = wordService.getWordByEnglish(english, getUsername(session));
        return words.size() > 0;
    }
}
