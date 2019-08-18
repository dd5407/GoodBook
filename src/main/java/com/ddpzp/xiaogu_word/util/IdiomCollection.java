package com.ddpzp.xiaogu_word.util;

import com.alibaba.fastjson.JSON;
import com.ddpzp.xiaogu_word.exception.GbException;
import com.ddpzp.xiaogu_word.po.game.Idiom;
import com.ddpzp.xiaogu_word.service.GameService;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 基于jsoup爬取成语
 * Created by dd
 * Date 2019/8/18 12:36
 */
@Component
@Slf4j
public class IdiomCollection {
    @Autowired
    private GameService gameService;

    /**
     * 爬取百度
     */
    public void baiduParser() {
        String url = "https://www.baidu.com/s?wd=成语&rsv_spt=1&rsv_iqid=0xe92c743200023604"
                + "&issp=1&f=8&rsv_bp=1&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_dl=ib&rsv_sug3=9"
                + "&rsv_sug1=8&rsv_sug7=101&rsv_sug2=0&inputT=1048&rsv_sug4=1735&rsv_sug=2";
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setTimeout(30000);
        try {
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
                Object nextPageBtn = page.getElementById("content_left")
                        .getFirstElementChild().getElementsByTagName("div").get(0)
                        .getFirstElementChild().getLastElementChild().getLastElementChild()
                        .getFirstElementChild().getLastElementChild();
                //没有下一页按钮，遍历结束，退出
                if (nextPageBtn == null) {
                    break;
                }
                HtmlSpan span = (HtmlSpan) nextPageBtn;
                //点击下一页，获取新页面
                page = span.click();
                pageNum++;
            }
            log.info("Parse baidu complete!");
        } catch (Exception e) {
            log.error("Baidu parser error!", e);
        } finally {
            webClient.close();
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
        //遍历a标签，取到每个标签中的title（成语）、链接（成语的搜索链接）
        for (Element a : idiomLinks) {
            Idiom idiom = new Idiom();
            //搜索链接（内链），该链接以该成语为搜索词，进行搜索
            String href = a.attr("href");
            //标题为成语
            String word = a.attr("title");
            if (word.length() != 4) {
                log.warn("[{}]不是四字成语，不录入！", word);
                continue;
            }
            //内链需要加上域名，组成完整链接
            href = "https://www.baidu.com" + href;
            log.info("===========<a> title={}, href={}============", word, href);
            //进入成语搜索页获取成语释义
            gotoLinkForIdiomMeans(href, idiom);
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
     * 通过成语链接，进入成语搜索页获取释义
     *
     * @param href  成语的搜索链接
     * @param idiom
     * @return
     */
    private void gotoLinkForIdiomMeans(String href, Idiom idiom) {
        try {
            Document doc = Jsoup.connect(href).get();
            //获取成语的百度百科链接
            String baikeHref = doc.select("#1 p[class=c-gap-top-small c-gap-bottom-small] a").attr("href");
            log.info("百度百科href:{}", baikeHref);
            //进入百度百科
            Document baikeDoc = Jsoup.connect(baikeHref).get();
            //注音拼音
            String phoneticPinyin = baikeDoc.select("#idiom-body div[class=content means imeans] dt").text();
            //成语完整释义
            String idiomMeans = baikeDoc.select("#idiom-body div[class=content means imeans] dd p").text().trim();
            idiom.setPhoneticPinyin(phoneticPinyin);
            idiom.setMeans(idiomMeans);
            log.info("注音拼音：{}", phoneticPinyin);
            log.info("释义：{}", idiomMeans);
        } catch (Exception e) {
            log.error("Parse [{}] failed!", href);
        }
    }
}
