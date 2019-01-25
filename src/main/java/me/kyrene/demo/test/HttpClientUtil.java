package me.kyrene.demo.test;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: zj
 * @Author: wanglin
 * @CreateDate: 2018/7/20 16:10
 */
public class HttpClientUtil {


        public static CloseableHttpResponse excute(Map<String,String> stringMap) throws URISyntaxException, IOException {

            //创建一个httpclient对象
            CloseableHttpClient httpClient = HttpClients.createDefault();
            //创建一个uri对象
            URIBuilder uriBuilder = new URIBuilder(stringMap.get("uri"));
            for (Map.Entry<String, String> entry : stringMap.entrySet()) {

                String key = entry.getKey();
                String value = entry.getValue();
                if(!"uri".equals(key)){
                    uriBuilder.addParameter(key,value);
                }

            }
            //        uriBuilder.addParameter("jyCode9001",stringMap.get("jyCode9001"));
            //        uriBuilder.addParameter("sourceCityCd",stringMap.get("sourceCityCd"));
            //        uriBuilder.addParameter("targetCityCd",stringMap.get("targetCityCd"));
            //        uriBuilder.addParameter("applyCode",stringMap.get("applyCode"));
            System.out.println(uriBuilder.toString());
            HttpGet get = new HttpGet(uriBuilder.build());
            //执行请求
            CloseableHttpResponse response = httpClient.execute(get);
            //取响应的结果
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("-------------- statusCode   : "+statusCode);
            return response;
        }

    public static CloseableHttpResponse excutePost(String uri, List<NameValuePair> formparams) throws URISyntaxException, IOException {

        //创建一个httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建一个uri对象
        UrlEncodedFormEntity uefEntity = null;
        CloseableHttpResponse response = null;

        HttpPost httpPost = new HttpPost(uri);
        uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
        //填充实体
        httpPost.setEntity(uefEntity);
        System.out.println("excuting request" + httpPost.getURI());
        //执行请求
        response = httpClient.execute(httpPost);

        return  response;
    }
}
