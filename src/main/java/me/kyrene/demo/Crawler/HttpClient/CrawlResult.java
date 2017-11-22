package me.kyrene.demo.Crawler.HttpClient;


import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;

/**
 * Created by wanglin on 2017/11/22.
 */
public class CrawlResult {

    private Status  status;

    private String url;

    private Document hrmlDoc;

    private Map<String,List<String>> result;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStatus(int code, String msg) {
        this.status = new Status(code, msg);
    }

    public Status getStatus(){
        return status;
    }

    static  class Status{
        private int code;

        private String msg;

        public Status(int code, String msg){
            this.code=code;
            this.msg=msg;
        }

        @Override
        public String toString() {
            return "Status{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    '}';
        }
    }
}
