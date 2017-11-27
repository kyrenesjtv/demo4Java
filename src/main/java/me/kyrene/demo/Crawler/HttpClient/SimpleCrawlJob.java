package me.kyrene.demo.Crawler.HttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 爬取任务类
 * Created by wanglin on 2017/11/22.
 */
public class SimpleCrawlJob extends AbstractJob {

    //配置项信息
    private CrawlMeta crawlMeta;

    //爬取的结果
    private CrawlResult crawlResult;

    public CrawlMeta getCrawlMeta() {
        return crawlMeta;
    }

    public void setCrawlMeta(CrawlMeta crawlMeta) {
        this.crawlMeta = crawlMeta;
    }

    public CrawlResult getCrawlResult() {
        return crawlResult;
    }

    public void setCrawlResult(CrawlResult crawlResult) {
        this.crawlResult = crawlResult;
    }

    @Override
    public void doFetchPage() throws Exception {
        //设置请求方式
        CrawlHttpConf httpConf = new CrawlHttpConf();
        httpConf.setMethod(CrawlHttpConf.HttpMethod.POST);

        HttpResponse response = HttpUtils.request(crawlMeta, httpConf);
        String res = EntityUtils.toString(response.getEntity());
        if (response.getStatusLine().getStatusCode() == 200) { // 请求成功

            //写入.txt文件
            File file = new File("C:\\Users\\Dell\\Desktop\\test.txt");
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(res);
            fileWriter.flush();
            fileWriter.close();
            //字符串只得到了2个回答？  知乎具有反爬虫机制
            doParse(res);
        } else {
            this.crawlResult = new CrawlResult();
            this.crawlResult.setStatus(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
            this.crawlResult.setUrl(crawlMeta.getUrl());
            throw new Exception("false!! " + crawlResult.getStatus().toString());
        }
    }

    private void doParse(String html) {

        Document document = Jsoup.parse(html);

        Map<String, List<String>> map = new HashMap<>(crawlMeta.getSelectorRules().size());
        List<String> authorNames = new ArrayList<>();
        String ListItem = crawlMeta.getSelectorRules().get(0);
        String RichContent = crawlMeta.getSelectorRules().get(1);
        String Avatar = crawlMeta.getSelectorRules().get(2);
       // Elements select = document.select(ListItem);
//        for (Element element : document.select(ListItem)) {
        Elements elements = document.select(ListItem);//为什么只有两个
        System.out.println("--------得到"+elements.size()+"个回答-------------");
        for (int j = 0; j < elements.size(); j++) {
            Element element = elements.get(j);
            List<String> list = new ArrayList<>();
            Elements img = element.select(RichContent).select("img");
            String authorName = element.select(Avatar).attr("alt");
            for (int i = 0; i < img.size(); i++) {
                if (i % 2 == 0) {
                    list.add(img.get(i).attr("src"));
                }
            }
            authorNames.add(authorName + j);
            map.put(authorName + j, list);
            System.out.println("------------第"+j+"个回答有"+list.size()+"张照片--------");
        }
        this.crawlResult = new CrawlResult();
        this.crawlResult.setUrl(crawlMeta.getUrl());
        this.crawlResult.setHrmlDoc(document);
        this.crawlResult.setResult(map);
        this.crawlResult.setAuthorNames(authorNames);
    }
}
