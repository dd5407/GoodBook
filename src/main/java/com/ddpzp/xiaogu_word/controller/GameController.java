package com.ddpzp.xiaogu_word.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ddpzp.xiaogu_word.common.Constants;
import com.ddpzp.xiaogu_word.exception.GbException;
import com.ddpzp.xiaogu_word.model.JsonResult;
import com.ddpzp.xiaogu_word.po.game.Frog;
import com.ddpzp.xiaogu_word.po.game.GuessIdiom;
import com.ddpzp.xiaogu_word.po.game.Idiom;
import com.ddpzp.xiaogu_word.po.game.LotteryItem;
import com.ddpzp.xiaogu_word.service.GameService;
import com.ddpzp.xiaogu_word.util.NlpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 * Date 2019/7/30 22:13
 */
@Slf4j
@RequestMapping("/gu/game/")
@Controller
public class GameController extends BaseController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpSession session;
    @Autowired
    private GameService gameService;

    /**
     * 数青蛙
     *
     * @param startNum 从第几只开始数
     * @param size     数几只
     * @return
     */
    @GetMapping(value = "frogCount")
    @ResponseBody
    public JsonResult frogCount(@RequestParam(required = false, defaultValue = "1") Integer startNum,
                                @RequestParam(required = false, defaultValue = "100") Integer size,
                                @RequestParam(required = false) Boolean crazyMode) {
        if (startNum <= 0 || size < 0) {
            return JsonResult.error("参数错误！");
        }
        if (startNum + size > 10000 && !crazyMode) {
            return JsonResult.error("大佬，你数不完的！");
        }
        try {
            List<Frog> list = gameService.countFrog(startNum, size);
            log.info("Count frog success! startNum:[{}],size:[{}],user:[{}].",
                    startNum, size, getUsername(session));
            return JsonResult.success(list);
        } catch (Exception e) {
            log.error("Count frog failed!", e);
            return JsonResult.error(String.format("数青蛙失败！%s", e.getMessage()));
        }
    }

    /**
     * 跳转数青蛙页面
     *
     * @param model
     * @return
     */
    @GetMapping("/page/countFrog")
    public String frogCountPage(Model model) {
        String username = getUsername(session);
        log.info("Count frog,username={}", username);
        List<Frog> list = gameService.countFrog(1, 100);
        model.addAttribute("frogList", list);
        return "countFrog";
    }

    /**
     * 跳转成语页面
     *
     * @return
     */
    @GetMapping("/page/idiom")
    public String idiomPage() {
        return "idiom";
    }

    @GetMapping("/randomIdiom")
    @ResponseBody
    public JsonResult randomIdiom() {
        try {
            Idiom idiom = gameService.randomIdiom();
            return JsonResult.success(idiom);
        } catch (GbException ge) {
            log.warn(ge.getMessage());
            return JsonResult.error(ge.getMessage());
        } catch (Exception e) {
            log.error("随机获取成语失败！", e);
            return JsonResult.error(e.getMessage());
        }
    }

    @GetMapping("/idiomLoong")
    @ResponseBody
    public JsonResult idiomLoong(String queryWord, Integer wordIndex) {
        if (StringUtils.isBlank(queryWord)) {
            return JsonResult.error("你要我接啥？");
        }
        queryWord = queryWord.trim();
        int wordLength = queryWord.length();
        if (wordLength > 4 && NlpUtil.isChinese(queryWord)) {
            log.warn("Invalid query word，queryWord=[{}]");
            return JsonResult.error("字太多，接不了！（输入4字成语，或者输入要接的字~）");
        }
        if (wordLength != 1 && wordLength != 4 && NlpUtil.isChinese(queryWord)) {
            log.warn("Invalid query word，queryWord=[{}]");
            return JsonResult.error("接不了，接出来算我输！要么输4个字，要么输要接的字~");
        }
        String username = getUsername(session);
        try {
            Idiom idiom = gameService.idiomLoong(queryWord, wordIndex);
            log.info("成语接龙，wordIndex=[{}],queryWord=[{}],next idiom=[{}],username={}",
                    wordIndex, queryWord, idiom.getWord(), username);
            return JsonResult.success(idiom);
        } catch (GbException ge) {
            log.warn(ge.getMessage());
            return JsonResult.error(ge.getMessage());
        } catch (Exception e) {
            log.error("Idiom loong error！username={}", username, e);
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 提交猜成语题目
     *
     * @param toUsername
     * @param guessIdiom
     * @return
     */
    @PostMapping("/sendIdiom")
    @ResponseBody
    public JsonResult sendIdiom(String toUsername, String guessIdiom) {
        if (StringUtils.isBlank(toUsername)) {
            log.warn("Param toUsername is empty！");
            return JsonResult.error("用户名不能为空！");
        }
        if (StringUtils.isBlank(guessIdiom)) {
            log.warn("Param guessIdiom is empty!");
            return JsonResult.error("成语不能为空！");
        }
        try {
            gameService.sendIdiom(getUsername(session), toUsername.trim(), guessIdiom.trim());
            return JsonResult.success();
        } catch (GbException ge) {
            log.warn(ge.getMessage());
            return JsonResult.error(ge.getMessage());
        } catch (Exception e) {
            log.error("Send idiom error!", e);
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 猜成语，答题者猜
     *
     * @param id
     * @param idiom
     * @return
     */
    @PostMapping("/guessIdiom")
    @ResponseBody
    public JsonResult guessIdiom(Integer id, String idiom) {
        if (id == null) {
            log.error("Param id is null!");
            return JsonResult.error("系统错误！id为空，请刷新页面后再试！");
        }
        if (StringUtils.isBlank(idiom)) {
            log.warn("Param idiom is empty!");
            return JsonResult.error("成语不能为空！");
        }
        try {
            gameService.guessIdiom(id, idiom.trim(), getUsername(session));
            return JsonResult.success();
        } catch (GbException ge) {
            log.warn(ge.getMessage());
            return JsonResult.error(ge.getMessage());
        } catch (Exception e) {
            log.error("Send idiom error!", e);
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 获取列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/getGuessIdiomList")
    @ResponseBody
    public JsonResult getGuessIdiomList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        try {
            String username = getUsername(session);
            List<GuessIdiom> list = gameService.getGuessIdiomList(page, pageSize, username);
            Integer total = gameService.countGuessIdiom(username);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", list);
            jsonObject.put("total", total);
            jsonObject.put("loginUser", username);
            log.info("Get guess idiom list success! page={},pageSize={},total={},username={}",
                    page, pageSize, total, username);
            return JsonResult.success(jsonObject);
        } catch (Exception e) {
            log.error("Get guess idiom list failed!", e);
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 删除猜成语题目
     *
     * @param id
     * @return
     */
    @PostMapping("/deleteGuessIdiom")
    @ResponseBody
    public JsonResult deleteGuessIdiom(Integer id) {
        try {
            if (id == null) {
                log.error("Id is null!");
                return JsonResult.error("id不能为null！");
            }
            String username = getUsername(session);
            gameService.deleteGuessIdiom(id, username);
            log.info("Delete guess idiom success! id={},username={}", id, username);
            return JsonResult.success();
        } catch (GbException ge) {
            log.warn(ge.getMessage());
            return JsonResult.error(ge.getMessage());
        } catch (Exception e) {
            log.error("Delete guess idiom failed!", e);
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 获取猜成语题目详情
     *
     * @param id
     * @return
     */
    @GetMapping("/getGuessIdiomDetail")
    @ResponseBody
    public JsonResult getGuessIdiomDetail(Integer id) {
        try {
            if (id == null) {
                log.error("Id is null!");
                return JsonResult.error("id不能为null！");
            }
            GuessIdiom guessIdiom = gameService.getGuessIdiomDetail(id);
            if (guessIdiom == null) {
                log.error("Guess idiom not exist! id={}", id);
                return JsonResult.error("题目不存在，请刷新页面后再试！");
            }
            return JsonResult.success(guessIdiom);
        } catch (Exception e) {
            log.error("Get guess idiom detail failed!", e);
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 放弃答题
     *
     * @param id
     * @return
     */
    @PostMapping("/abandonGuessIdiom")
    @ResponseBody
    public JsonResult abandonGuessIdiom(Integer id) {
        try {
            gameService.abandonGuessIdiom(id, getUsername(session));
            return JsonResult.success();
        } catch (GbException ge) {
            log.warn(ge.getMessage());
            return JsonResult.error(ge.getMessage());
        } catch (Exception e) {
            log.error("Abandon guess idiom failed!", e);
            return JsonResult.error(e.getMessage());
        }
    }

    /**
     * 成语查询
     *
     * @param idiom
     * @return
     */
    @GetMapping("/queryIdiom")
    @ResponseBody
    public JsonResult queryIdiom(String idiom) {
        try {
            if (StringUtils.isBlank(idiom)) {
                log.warn("Query idiom is blank!");
                return JsonResult.error("啥也没输？");
            }
            Idiom result = gameService.queryIdiom(idiom);
            return JsonResult.success(result);
        } catch (GbException ge) {
            log.warn(ge.getMessage());
            return JsonResult.error(ge.getMessage());
        } catch (Exception e) {
            log.error("Query idiom failed!", e);
            return JsonResult.error(e.getMessage());
        }
    }

    @GetMapping(value = "page/lotteryList")
    public String lotteryListPage(Model model) {
        try {
            String username = getUsername(session);
            log.info("Get lottery item list,username={}", username);
            List<LotteryItem> lotteryItems = gameService.getLotteryItems(username, null, null);
            Integer total = gameService.countLotteryItems(username);
            model.addAttribute("lotteryItems", lotteryItems);
            model.addAttribute("current", 1);
            model.addAttribute("total", total);
        } catch (Exception e) {
            log.error("获取抽奖列表失败！", e);
            model.addAttribute(Constants.ERROR_MSG_KEY, String.format("获取抽奖列表失败,错误信息：[%s]", e.getMessage()));
        }
        return "lottery";
    }

    @GetMapping(value = "lottery/itemList")
    @ResponseBody
    public JsonResult lotteryList(@RequestParam(required = false, defaultValue = "1") Integer current,
                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        try {
            String username = getUsername(session);
            current = null;
            pageSize = null;
            List<LotteryItem> lotteryItems = gameService.getLotteryItems(username, current, pageSize);
            Integer total = gameService.countLotteryItems(username);
            JSONObject obj = new JSONObject();
            obj.put("data", lotteryItems);
            obj.put("total", total);
            return JsonResult.success(obj);
        } catch (Exception e) {
            log.error("获取抽奖列表失败！", e);
            return JsonResult.error(String.format("获取抽奖列表失败！错误信息：[%s]", e.getMessage()));
        }
    }

    @PostMapping(value = "lottery/addItem")
    @ResponseBody
    public JsonResult addLotteryItem(@RequestBody LotteryItem lotteryItem) {
        String name = lotteryItem.getName();
        if (StringUtils.isBlank(name)) {
            return JsonResult.error("请填写选项！");
        }
        String[] nameArr = name.split(",");
        try {
            for (String singleName : nameArr) {
                addLotteryItem(singleName);
            }
            return JsonResult.success();
        } catch (GbException ge) {
            log.warn(ge.getMessage());
            return JsonResult.error(ge.getMessage());
        } catch (Exception e) {
            log.error("添加抽奖选项失败!", e);
            return JsonResult.error(e.getMessage());
        }
    }

    private void addLotteryItem(String name) throws GbException {
        if (StringUtils.isBlank(name)) {
            throw new GbException("选项格式有误！");
        }
        LotteryItem lotteryItem = new LotteryItem();
        lotteryItem.setName(name);
        lotteryItem.setCreateTime(new Date());
        lotteryItem.setCreator(getUsername(session));

        List<LotteryItem> lotteryItems = gameService.getLotteryItemByName(name, getUsername(session));
        if (lotteryItems.size() > 0) {
            throw new GbException(String.format("选项[%s]已存在！", name));
        }
        gameService.addLotteryItem(lotteryItem);
    }

    @PostMapping(value = "lottery/updateItem")
    @ResponseBody
    public JsonResult updateLotteryItem(@RequestBody LotteryItem lotteryItem) {
        if (StringUtils.isBlank(lotteryItem.getName())) {
            return JsonResult.error("请填写选项！");
        }

        Integer id = lotteryItem.getId();
        String wordJsonFromRequest = JSON.toJSONString(lotteryItem);
        if (id == null) {
            log.error("Update word error, id is null! {}", wordJsonFromRequest);
            return JsonResult.error("id不存在，请刷新列表后再试！");
        }
        try {
            LotteryItem itemInDatabase = gameService.getLotteryItem(id);
            if (itemInDatabase == null) {
                log.error("Update lottery item error,item is not exists! Request item:[{}]", wordJsonFromRequest);
                return JsonResult.error("该选项不存在！请刷新列表后再试！");
            }
            //未做修改，直接返回成功
            if (StringUtils.equals(itemInDatabase.getName(), lotteryItem.getName())) {
                return JsonResult.success();
            }
            //将修改存入数据库
            gameService.updateLotteryItem(lotteryItem);
            return JsonResult.success();
        } catch (Exception e) {
            log.error("修改抽奖选项失败！", e);
            return JsonResult.error(String.format("修改选项失败，错误信息：[%s]", e.getMessage()));
        }
    }

    @PostMapping(value = "lottery/deleteItem")
    @ResponseBody
    public JsonResult deleteLotteryItem(Integer id) {
        try {
            if (id == null) {
                log.error("id不能为空");
                return JsonResult.error("id不能为空！");
            }
            LotteryItem lotteryItem = gameService.getLotteryItem(id);
            if (lotteryItem == null) {
                log.error("抽奖选项不存在，id=[{}]", id);
                return JsonResult.error("选项不存在！");
            }
            gameService.deleteLotteryItem(id);
            log.info("用户[{}]删除抽奖选项[{}]成功！", getUsername(session), lotteryItem.getName());
            return JsonResult.success();
        } catch (Exception e) {
            log.error("删除抽奖选项失败！", e);
            return JsonResult.error(String.format("删除选项失败！错误信息：[%s]", e.getMessage()));
        }
    }

    @PostMapping("lottery/batchDelete")
    @ResponseBody
    public JsonResult batchDelete(Integer[] ids) {
        if (ids == null || ids.length <= 0) {
            return JsonResult.error("至少选择一项！");
        }
        try {
            gameService.batchDeleteLottery(ids);
            log.info("抽奖选项批量删除成功！id:[{}]", Arrays.toString(ids));
            return JsonResult.success();
        } catch (Exception e) {
            log.error("批量删除抽奖选项失败！", e);
            return JsonResult.error(String.format("批量删除选项失败！错误信息：[%s]", e.getMessage()));
        }
    }

    @GetMapping("lottery/getRandomItem")
    @ResponseBody
    public JsonResult randomLottery() {
        try {
            LotteryItem lotteryItem = gameService.lottery(getUsername(session));
            return JsonResult.success(lotteryItem);
        } catch (GbException ge) {
            log.warn(ge.getMessage());
            return JsonResult.error(ge.getMessage());
        } catch (Exception e) {
            log.error("抽奖失败!", e);
            return JsonResult.error(e.getMessage());
        }
    }
}
