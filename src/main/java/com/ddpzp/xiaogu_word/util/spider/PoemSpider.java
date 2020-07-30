package com.ddpzp.xiaogu_word.util.spider;

import com.alibaba.fastjson.JSON;
import com.ddpzp.xiaogu_word.exception.GbException;
import com.ddpzp.xiaogu_word.model.baiduHanyu.BaiduHanyuPoem;
import com.ddpzp.xiaogu_word.model.baiduHanyu.BaiduHanyuPoemPage;
import com.ddpzp.xiaogu_word.po.game.Poem;
import com.ddpzp.xiaogu_word.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 古诗词爬虫
 * Created by dd
 * Date 2020/7/29 23:29
 */
@Component
@Slf4j
public class PoemSpider {
    @Autowired
    private GameService gameService;

    public void collect() {
        log.info("开始运行诗词爬虫！");
        parseBaiduHanYu();
        log.info("诗词爬虫完成!");
    }

    private void parseBaiduHanYu() {
        //百度汉语-诗词，去掉了页码
        String url = "https://hanyu.baidu.com/hanyu/ajax/search_list?wd=%E8%AF%97%E8%AF%8D&device=pc&from=home&pn=";
        log.info("开始解析百度汉语-诗词！ url={}", url);
        try {
            Integer pageCount = getTotalPage(url + 1);
            for (int i = 1; i <= pageCount; i++) {
                log.info("BaiduHanyu poem spider process. current page={} and {} pages left", i, pageCount - i);
                //发送请求并解析返回数据，并往数据库添加成语
                processPage(url + i);
            }
            log.info("百度汉语-诗词解析完毕！");
        } catch (Exception e) {
            log.error("百度汉语-诗词解析错误！", e);
        }
    }

    /**
     * 请求第一页来获取总页数
     *
     * @param firstUrl
     * @return
     * @throws IOException
     */
    private Integer getTotalPage(String firstUrl) throws IOException {
        String json = Jsoup.connect(firstUrl).ignoreContentType(true).execute().body();
        BaiduHanyuPoemPage page = JSON.parseObject(json, BaiduHanyuPoemPage.class);
        Map<String, Integer> extra = page.getExtra();
        log.info("诗词总数：{}，总页数：{}", extra.get("entity-num"), extra.get("total-page"));
        return extra.get("total-page");
    }

    /**
     * 解析列表页
     *
     * @param ajaxDataUrl
     */
    private void processPage(String ajaxDataUrl) {
        String json = null;
        try {
            json = Jsoup.connect(ajaxDataUrl).ignoreContentType(true).execute().body();
            BaiduHanyuPoemPage page = JSON.parseObject(json, BaiduHanyuPoemPage.class);
            List<BaiduHanyuPoem> retArray = page.getRet_array();
            for (BaiduHanyuPoem baiduPoem : retArray) {
                Poem poem = new Poem();
                String title;
                String sid = null;
                Set<String> tags;
                try {
                    sid = baiduPoem.getSid().get(0);
                    title = baiduPoem.getDisplay_name().get(0).trim();
                    poem.setTitle(title);
                    poem.setAuthor(baiduPoem.getLiterature_author().get(0).trim());
                    poem.setBody(baiduPoem.getBody().get(0));
                    poem.setDynasty(baiduPoem.getDynasty().get(0).trim());
                    // 解析诗词详情页
                    tags = parseDetail(poem, sid);
                } catch (Exception e) {
                    log.error("诗词解析失败！sid:{},json:{}", sid, json, e);
                    continue;
                }
                try {
                    gameService.addPoem(poem, tags);
                } catch (GbException ge) {
                    log.warn("诗词[{}]添加失败！{}", title, ge.getMessage());
                } catch (Exception e) {
                    log.error("诗词[{}]添加失败！poem:{},tags:{}",
                            title, JSON.toJSONString(poem), JSON.toJSONString(tags), e);
                }
            }
        } catch (Exception e) {
            log.error("Baidu hanyu poem process page error！json:[{}]", json, e);
        }
    }

    /**
     * 解析诗词详情页
     *
     * @param poem
     * @param pid
     */
    private Set<String> parseDetail(Poem poem, String pid) throws IOException {
        String detailUrl = "https://hanyu.baidu.com/shici/detail?pid=" + pid;
        Document doc = Jsoup.connect(detailUrl).get();
        /* 标签 */
        Elements aList = doc.select(".poem-tags-content a");
        Set<String> tags = new HashSet<>();
        for (Element a : aList) {
            String tag = a.text().trim();
            tags.add(tag);
        }
        /* 译文 */
        String means = doc.select(".means-fold").text();
        if (StringUtils.length(means) > 4000) {
            log.info("means longer than 4000 words, cut it to 3997 words!");
            means = StringUtils.substring(means, 0, 3997) + "...";
        }
        poem.setMeans(means);
        /* 作者介绍 */
        Elements introEle = doc.select(".poem-author-intro");
        String authorDetail = introEle.select("span").text();
        String authorIntroLink = introEle.select("a").attr("href");
        poem.setAuthorDetail(authorDetail);
        poem.setAuthorBaikeLink(authorIntroLink);
        return tags;
    }
}
