package me.kyrene.demo.Crawler.HttpClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanglin on 2017/11/22.
 */
public class CrawlHttpConf {
    private static Map<String, String> DEFAULT_HEADERS;

    static  {
        DEFAULT_HEADERS = new HashMap<>();
        DEFAULT_HEADERS.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        DEFAULT_HEADERS.put("connection", "Keep-Alive");
        DEFAULT_HEADERS.put("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");
    }


    public enum HttpMethod {
        GET,
        POST,
        OPTIONS,
        PUT;
    }

    private HttpMethod method = HttpMethod.GET;

    private Map<String, String> requestHeaders;

    private Map<String, Object> requestParams;

    public HttpMethod getMethod() {
        return method;
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders == null ? DEFAULT_HEADERS : requestHeaders;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public Map<String, Object> getRequestParams() {
        return requestParams == null ? Collections.emptyMap() : requestParams;
    }

    public void setRequestParams(Map<String, Object> requestParams) {
        this.requestParams = requestParams;
    }

    @Override
    public String toString() {
        return "CrawlHttpConf{" +
                "method=" + method +
                ", requestHeaders=" + requestHeaders +
                ", requestParams=" + requestParams +
                '}';
    }
}
