package me.kyrene.demo.Crawler.HttpClient;


import java.util.*;

/**
 * Created by wanglin on 2017/11/22.
 */
public class Test {

    @org.junit.Test
    public void test01() throws InterruptedException {
        String url = "https://www.zhihu.com/question/35136922";
//        String url= "http://kyrene.me/";
        List<String> selectRule = new ArrayList<>();
        //回答的大致框架
        selectRule.add("div[class=List-item]");
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
        for(int i = 0 ;i<authorNames.size();i++){
            Map<String, List<String>> result1 = result.getResult(authorNames);
            //流写出到本地文件夹

        }

        System.out.println(result);
    }



}
