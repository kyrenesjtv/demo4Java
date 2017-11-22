package me.kyrene.demo.Crawler.JDKCraw;


import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;

/**
 * Created by wanglin on 2017/11/22.
 */
public class CrawlResult {
    //爬取的网址
    private String url;
    //爬去对应网址的html结构
    private Document hrmlDoc;
    //选择结果，key为选择规则，value为根据匹配的结果
    private Map<String,List<String>> result;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Document getHrmlDoc() {
        return hrmlDoc;
    }

    public void setHrmlDoc(Document hrmlDoc) {
        this.hrmlDoc = hrmlDoc;
    }

    public Map<String, List<String>> getResult() {
        return result;
    }

    public void setResult(Map<String, List<String>> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "CrawlResult{" +
                "url='" + url + '\'' +
                ", hrmlDoc=" + hrmlDoc +
                ", result=" + result +
                '}';
    }
}
