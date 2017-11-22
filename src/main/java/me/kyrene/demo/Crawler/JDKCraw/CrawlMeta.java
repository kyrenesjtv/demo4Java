package me.kyrene.demo.Crawler.JDKCraw;


import java.util.HashSet;
import java.util.Set;

/**
 * Created by wanglin on 2017/11/22.
 */
public class CrawlMeta {
    //要爬的网址
    private String url;
    //获取内容
    private Set<String> selectorRules;


    public void setSelectorRules(Set<String> selectorRules) {
        this.selectorRules = selectorRules;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 这么做的目的就是为了防止NPE, 也就是说支持不指定选择规则
    public Set<String> getSelectorRules() {
        return selectorRules != null ? selectorRules : new HashSet<>();
    }

    @Override
    public String toString() {
        return "CrawlMeta{" +
                "url='" + url + '\'' +
                ", selectorRules=" + selectorRules +
                '}';
    }
}
