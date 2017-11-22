package me.kyrene.demo.Crawler.JDKCraw;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

        URL url = new URL(crawlMeta.getUrl());
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        BufferedReader  reader = null ;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            // 设置通用的请求属性
            urlConnection.setRequestProperty("accept", "*/*");
            urlConnection.setRequestProperty("connection", "Keep-Alive");
            urlConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            urlConnection.connect();
            Map<String, List<String>> map = urlConnection.getHeaderFields();
            //遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            reader= new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) !=null){
                //读取到了所有的页面
                stringBuilder.append(line);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(reader != null ) {
                    reader.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        doParse(stringBuilder.toString());
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
