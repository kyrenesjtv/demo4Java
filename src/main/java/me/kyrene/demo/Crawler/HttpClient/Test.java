package me.kyrene.demo.Crawler.HttpClient;


import me.kyrene.demo.io.FileUtils;

import java.io.File;
import java.util.*;

/**
 * Created by wanglin on 2017/11/22.
 */
public class Test {

    @org.junit.Test
    public void test01() throws InterruptedException {
        String url = "https://www.zhihu.com/question/35136922";
        String downLoadUrl = "C:\\Users\\Dell\\Desktop\\zhihu";
//        String url= "http://kyrene.me/";
        List<String> selectRule = new ArrayList<>();
        //回答的大致框架
       // selectRule.add("div[class=List-item]");
        selectRule.add("div[class=ContentItem AnswerItem]");
        selectRule.add("div[class=RichContent RichContent--unescapable]");
        selectRule.add("img[class=Avatar AuthorInfo-avatar]");
        CrawlMeta crawlMeta = new CrawlMeta();
        crawlMeta.setUrl(url); // 设置爬取的网址
        crawlMeta.setSelectorRules(selectRule); // 设置抓去的内容

        SimpleCrawlJob job = new SimpleCrawlJob();
        job.setCrawlMeta(crawlMeta);
        Thread thread = new Thread(job, "crawler-test");
        thread.start();
        thread.join(); // 确保线程执行完毕
        CrawlResult result = job.getCrawlResult();
        List<String> authorNames = result.getAuthorNames();
        Map<String, List<String>> answers = result.getResult();
        for (int i = 0; i < authorNames.size(); i++) {
            String name = authorNames.get(i);
            List<String> strings = answers.get(authorNames.get(i));
            //流写出到本地文件夹
            System.out.println("---------下载第"+i+"个回答--------------");
            for (int j = 0; j < strings.size(); j++) {
                FileUtils.downloadPicture(strings.get(j), downLoadUrl + "\\" + authorNames.get(i));
            }
        }
        System.out.println(result);
    }


}
