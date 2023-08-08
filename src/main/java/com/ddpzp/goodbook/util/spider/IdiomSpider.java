package com.ddpzp.goodbook.util.spider;

import com.alibaba.fastjson.JSON;
import com.ddpzp.goodbook.exception.GbException;
import com.ddpzp.goodbook.model.baiduHanyu.BaiduHanyuIdiom;
import com.ddpzp.goodbook.model.baiduHanyu.BaiduHanyuIdiomPage;
import com.ddpzp.goodbook.po.game.Idiom;
import com.ddpzp.goodbook.service.GameService;
import com.ddpzp.goodbook.util.WebClientFactory;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 基于htmlUnit和jsoup爬取成语
 * Created by dd
 * Date 2019/8/18 12:36
 */
@Component
@Slf4j
public class IdiomSpider {
    @Autowired
    private GameService gameService;

    /**
     * 爬取百度
     */
    public void baiduParser() {
        String[] urls = {"https://www.baidu.com/s?wd=成语&rsv_spt=1&rsv_iqid=0xe92c743200023604&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_dl=ib&rsv_sug3=9&rsv_sug1=8&rsv_sug7=101&rsv_sug2=0&inputT=1048&rsv_sug4=1735&rsv_sug=2"
                , "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=心理成语&oq=%25E5%259B%259B%25E5%25AD%2597%25E6%2588%2590%25E8%25AF%25AD&rsv_pq=98a9960800009368&rsv_t=d786CBPV0YzX9iHrILRlNlUcrI24cchgnyfJDGEm39WkfJQHTiTEp6ehq5M&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=5122&rsv_sug3=44&rsv_sug1=32&rsv_sug7=100&rsv_sug2=0&rsv_sug4=5122"
                , "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=动物成语&oq=%25E5%25BF%2583%25E7%2590%2586%25E6%2588%2590%25E8%25AF%25AD&rsv_pq=d824d1e5000091b4&rsv_t=27dfUZN11Urcb2b8hzGrCPeqORME3%2B4m%2BnSdMBH0vtUIlIH56UhIKkRZZ48&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=5122&rsv_sug3=51&rsv_sug1=38&rsv_sug7=100&bs=心理成语"
                , "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=植物成语&oq=%25E5%258A%25A8%25E7%2589%25A9%25E6%2588%2590%25E8%25AF%25AD&rsv_pq=809ed14700085972&rsv_t=0818jZveCw5BT3oo%2FGpI0R1o2sZnSwIukYoZJLcSHUZQ0apDf5%2FgHt8Awx0&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=665173&rsv_sug3=57&rsv_sug1=41&rsv_sug7=100&bs=动物成语"
                , "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=aabb成语&oq=abac%25E6%2588%2590%25E8%25AF%25AD&rsv_pq=fe8e58580000ede2&rsv_t=9a99FmGe6tMdI3gjqmqWu3i8cTuvJd8%2FQ6PY%2FwW2GaPWaD9BKxHG5mrm99I&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=40725&rsv_sug3=87&rsv_sug1=71&rsv_sug7=100&bs=abac成语"
                , "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=abac成语&oq=aabb%25E6%2588%2590%25E8%25AF%25AD&rsv_pq=b6eb530c0000db48&rsv_t=87d3dOx8tQ2ge%2FmvKsew1jmCZJSlsPFZq3%2FDk1rtrVFag2xb4HJCWgn5A0E&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=1386&rsv_sug3=76&rsv_sug1=60&rsv_sug7=100&bs=aabb成语"
                , "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=aabc成语&oq=abac%25E6%2588%2590%25E8%25AF%25AD&rsv_pq=fe8e58580000ede2&rsv_t=c9489%2FvjSg5L6jeqH%2BPQXmbdRYX%2FX%2BYEqnQlAddnu0eSrZJlcqVNqivRguY&rqlang=cn&rsv_enter=1&rsv_dl=tb&inputT=40725&rsv_sug3=85&rsv_sug1=69&rsv_sug7=100&bs=abac成语"};

        WebClient webClient = WebClientFactory.getInstance();
        for (String url : urls) {
            parseSearchUrl(webClient, url);
        }
        webClient.close();
        log.info("Parse baidu complete!");
    }

    /**
     * 使用htmlUnit解析每个url（百度搜索页）
     *
     * @param url
     */
    private void parseSearchUrl(WebClient webClient, String url) {
        try {
            log.info("Parse baidu search url start! url={}", url);
            //先使用httpUnit获取页面（以便点击按钮动态js翻页，jsoup只能获取静态页面，不能点击按钮进行翻页）
            HtmlPage page = webClient.getPage(url);
            webClient.waitForBackgroundJavaScript(30000);
            int pageNum = 1;
            while (true) {
                log.info("第{}页", pageNum);
                String htmlStr = page.asXml();
                //使用jsoup解析html页面字符串
                Document doc = Jsoup.parse(htmlStr);
                //使用jsoup解析静态页面，遍历当页的成语
                parseEachPage(doc);
                //获取下一页按钮->span标签
                DomElement opuiPage = page.getElementById("content_left")
                        .getElementsByTagName("div").get(0).getElementsByTagName("div").get(0)
                        .getFirstElementChild().getLastElementChild().getLastElementChild().getFirstElementChild();
                // 没有页码栏，只有一页，遍历结束，退出
                if (opuiPage == null) {
                    break;
                }
                DomElement nextPageBtn = opuiPage.getLastElementChild();
                HtmlSpan span = (HtmlSpan) nextPageBtn;
                //最后一页
                if (StringUtils.equals(span.getAttribute("style"), "visibility:hidden;")) {
                    log.info("The last page over!");
                    break;
                }
                //点击下一页，获取新页面
                page = span.click();
                pageNum++;
            }
            log.info("Parse url complete! url={}", url);
        } catch (Exception e) {
            log.error("Parse url error! url={}", url, e);
        }
    }

    /**
     * 遍历当前页的成语，进行解析
     *
     * @param doc 当前页面的dom
     */
    private void parseEachPage(Document doc) {
        //获取页面中的每个成语，成语为a标签
        Elements idiomLinks = doc.select("#content_left "
                + "div[class=op_exactqa_itemsArea c-row  c-gap-bottom-small] "
                + ".op_exactqa_item a");
        //遍历a标签，取到每个标签中的title（成语）、链接（成语详情页链接）
        for (Element a : idiomLinks) {
            Idiom idiom = new Idiom();
            //成语详情页链接
            String href = a.attr("href");
            //标题为成语
            String word = a.attr("title").trim();
            if (word.length() != 4) {
                log.warn("[{}]不是四字成语，不录入！", word);
                continue;
            }
            log.info("[成语详情页链接] word={}, href={}", word, href);
            //进入成语详情页获取成语释义
            parseBaiduHanyu(idiom, href);
            idiom.setWord(word);
            try {
                gameService.addIdiom(idiom);
            } catch (GbException ge) {
                log.warn("{}{}", ge.getMessage(), JSON.toJSONString(idiom));
            } catch (Exception e) {
                log.error("Add idiom failed! {}", JSON.toJSONString(idiom), e);
            }
        }
    }

    /**
     * 解析百度汉语成语详情页面，获取释义和注音拼音
     *
     * @param idiom
     * @param idiomDetailHref
     * @throws IOException
     */
    private void parseBaiduHanyu(Idiom idiom, String idiomDetailHref) {
        if (StringUtils.isEmpty(idiomDetailHref)) {
            log.warn("成语详情页链接为空！");
            return;
        }
        try {
            //进入百度汉语
            Document baiduHanyuDoc = Jsoup.connect(idiomDetailHref).get();
            //注音拼音
            String phoneticPinyin = baiduHanyuDoc.select("#idiom-body div[class=content means imeans] dt").text().trim();
            if (StringUtils.isEmpty(phoneticPinyin)) {
                phoneticPinyin = baiduHanyuDoc.select("#basicmean-wrapper .pinyin").text().trim();
            }
            //成语完整释义
            String idiomMeans = baiduHanyuDoc.select("#idiom-body div[class=content means imeans] dd p").text().trim();
            if (StringUtils.isEmpty(idiomMeans)) {
                idiomMeans = baiduHanyuDoc.select("#basicmean-wrapper dd p").text().trim();
            }
            if (StringUtils.isEmpty(idiomMeans)) {
                String baikeMeans = getBaikeMeansWithoutDefinition(idiomDetailHref);
                idiomMeans = StringUtils.isBlank(baikeMeans) ? "暂无" : baikeMeans;
            }
            idiom.setPhoneticPinyin(phoneticPinyin);
            idiom.setMeans(idiomMeans);
        } catch (Exception e) {
            log.error("Parse idiom detail link failed! href={}", idiomDetailHref);
        }
    }

    /**
     * 百度汉语爬虫，爬首页成语页面
     */
    public void hanyuIdiomSpider() {
        //ajax获取数据的url前缀，去除页码
        String ajaxDataUrlPrefix = "https://hanyu.baidu.com/hanyu/ajax/search_list?wd=%E6%88%90%E8%AF%AD&device=pc&from=home&pn=";
        try {
            log.info("Baidu hanyu idiom spider start...");
            //获取总页数
            Integer pageCount = getTotalPage(ajaxDataUrlPrefix + 1);
            for (int i = 1; i <= pageCount; i++) {
                log.info("BaiduHanyu spider process. current page={} and {} pages left", i, pageCount - i);
                //发送请求并解析返回数据，并往数据库添加成语
                processPage(ajaxDataUrlPrefix + i);
            }
            log.info("Baidu hanyu idiom spider process complete！");
        } catch (Exception e) {
            log.error("百度汉语爬取失败！", e);
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
        BaiduHanyuIdiomPage page = JSON.parseObject(json, BaiduHanyuIdiomPage.class);
        Map<String, Integer> extra = page.getExtra();
        log.info("成语总数：{}，每页条数：{}，总页数：{}", extra.get("entity-num"),
                extra.get("return-num"), extra.get("total-page"));
        return extra.get("total-page");
    }

    private void processPage(String ajaxDataUrl) {
        String json = null;
        try {
            json = Jsoup.connect(ajaxDataUrl).ignoreContentType(true).execute().body();
            BaiduHanyuIdiomPage page = JSON.parseObject(json, BaiduHanyuIdiomPage.class);
            List<BaiduHanyuIdiom> retArray = page.getRet_array();
            for (BaiduHanyuIdiom baiduIdiom : retArray) {
                String word = baiduIdiom.getName().get(0).trim();
                String means = baiduIdiom.getDefinition().get(0).trim();
                String phoneticPinyin = baiduIdiom.getPinyin().get(0).trim();
                if (means.length() <= 0) {
                    //如果没有释义，尝试去百度汉语页面获取百科释义
                    String baikeMeans = getBaikeMeansWithoutDefinition(baiduIdiom.getBasic_source_url().get(0));
                    if (StringUtils.isBlank(baikeMeans)) {
                        means = "暂无";
                    } else {
                        means = baikeMeans;
                    }
                } else if (means.length() > 1000) {
                    means = means.substring(0, 997) + "...";
                }
                Idiom idiom = new Idiom();
                idiom.setWord(word);
                idiom.setPhoneticPinyin("[ " + phoneticPinyin + " ]");
                idiom.setMeans(means);
                try {
                    gameService.addIdiom(idiom);
                } catch (GbException ge) {
                    log.warn("成语[{}]添加失败！{}", word, ge.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Baidu hanyu process page error！json:[{}]", json, e);
        }
    }

    /**
     * 在百度汉语页面获取百科释义
     *
     * @param hanyuUrl
     * @return
     */
    private String getBaikeMeansWithoutDefinition(String hanyuUrl) {
        try {
            Document doc = Jsoup.connect(hanyuUrl).get();
            String baikeMeans = doc.select("#baike-wrapper .tab-content p").text();
            return baikeMeans.trim();
        } catch (Exception e) {
            log.error("Get baike means failed!", e);
        }
        return null;
    }
}
