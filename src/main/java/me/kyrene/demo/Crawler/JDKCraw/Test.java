package me.kyrene.demo.Crawler.JDKCraw;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by wanglin on 2017/11/22.
 */
public class Test {

    @org.junit.Test
    public void test01() throws InterruptedException {
//        String url = "https://my.oschina.net/u/566591/blog/1031575";
        String url= "http://kyrene.me/";
        Set<String> selectRule = new HashSet<>();
        selectRule.add("h2"); // 连接地址
//        selectRule.add("div[class=blog-body]"); // 博客正文

        CrawlMeta crawlMeta = new CrawlMeta();
        crawlMeta.setUrl(url); // 设置爬取的网址
        crawlMeta.setSelectorRules(selectRule); // 设置抓去的内容


        SimpleCrawlJob job = new SimpleCrawlJob();
        job.setCrawlMeta(crawlMeta);
        Thread thread = new Thread(job, "crawler-test");
        thread.start();

        thread.join(); // 确保线程执行完毕


        CrawlResult result = job.getCrawlResult();
        System.out.println(result);

    }
}
