package me.kyrene.demo.Crawler.HttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
        httpConf.setMethod(CrawlHttpConf.HttpMethod.GET);

        HttpResponse response = HttpUtils.request(crawlMeta, httpConf);
        String res = EntityUtils.toString(response.getEntity());
        if (response.getStatusLine().getStatusCode() == 200) { // 请求成功
            doParse(res);
        } else {
            this.crawlResult = new CrawlResult();
            this.crawlResult.setStatus(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase());
            this.crawlResult.setUrl(crawlMeta.getUrl());
            throw new Exception("false!! "+crawlResult.getStatus().toString());
        }
    }
    private void doParse(String html) {

        Document document = Jsoup.parse(html);

        Map<String, List<String>> map = new HashMap<>(crawlMeta.getSelectorRules().size());

        for (String rule : crawlMeta.getSelectorRules()) {
            List<String> list = new ArrayList<>();
            for (Element element: document.select(rule)) {
                //我需要在这里硬编码?????  知乎小姐姐啊啊啊啊啊
                Elements a = element.select("a");
                list.add(a.attr("href"));
                System.out.println(list.toString());
            }
            map.put(rule,list);
        }
        this.crawlResult = new CrawlResult();
        this.crawlResult.setUrl(crawlMeta.getUrl());
        this.crawlResult.setHrmlDoc(document);
        this.crawlResult.setResult(map);
    }
}
