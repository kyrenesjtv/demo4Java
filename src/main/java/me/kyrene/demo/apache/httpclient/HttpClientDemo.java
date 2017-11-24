package me.kyrene.demo.apache.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin on 2017/10/15.
 */
public class HttpClientDemo {
    @Test
    public void test1() throws IOException {
        //httpClient实例
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://www.baidu.com");
        //执行http get请求
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            //获取响应实体
            HttpEntity httpEntity = httpResponse.getEntity();
            //打印响应状态
            System.out.println("响应状态码" + httpResponse.getStatusLine());
            //判断实体是否为空
            if (httpEntity != null) {
                System.out.println("编码" + httpEntity.getContentEncoding());
                System.out.println("html内容" + EntityUtils.toString(httpEntity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpClient.close();
        }
    }

    @Test
    public void sendPost() throws IOException {
        //创建http实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建http post
        HttpPost httpPost = new HttpPost("http://share0.oaw.me:12345");
        //创建参数  nameValuePair 是自带的
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("username", "admin"));
        formparams.add(new BasicNameValuePair("password", "123"));
        UrlEncodedFormEntity uefEntity = null;
        CloseableHttpResponse response = null;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            //填充实体
            httpPost.setEntity(uefEntity);
            System.out.println("excuting request" + httpPost.getURI());
            //执行请求
            response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                System.out.println("Respone Content" + EntityUtils.toString(httpEntity, "UTF-8"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
    }

    @Test
    public void upLoad() {
        //创建httpclient 实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //post 对象
        HttpPost httpPost = new HttpPost("");
        FileBody fileBody = new FileBody(new File(""));
        StringBody stringBody = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);
        //上传对象
        HttpEntity multipartEntityBuilder = (HttpEntity) MultipartEntityBuilder.create().addPart("bin", fileBody).addPart("comment", stringBody);
        httpPost.setEntity(multipartEntityBuilder);
        CloseableHttpResponse response = null;
        System.out.println("excuting request" + httpPost.getRequestLine());
        try {
            //执行
            response = httpClient.execute(httpPost);
            System.out.println("status" + response.getStatusLine());
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                System.out.println("content length:" + entity.getContentLength());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(httpClient);
            HttpClientUtils.closeQuietly(response);
        }

    }

    @Test
    public void setHead() {
        //创建http实例
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //创建参数  nameValuePair 是自带的
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("username", "admin"));
        formparams.add(new BasicNameValuePair("password", "123"));
        // UrlEncodedFormEntity uefEntity = null;
        CloseableHttpResponse response = null;
        HttpEntity entity = EntityBuilder.create().setContentType(ContentType.create("application/x-www-form-urlencoded", "UTF-8")).setParameters(formparams).build();
        HttpUriRequest request = RequestBuilder.post().setConfig(RequestConfig.DEFAULT).setHeader("Content-Type", "application/x-www-form-urlencoded").setHeader("User-Agent", "I am superman").setUri("http://uux.me").setEntity(entity).build();
         try {
            //执行请求
             System.out.println("excuting request:"+request.getRequestLine());
            response = httpClient.execute(request);
             //得到响应头
             Header[] allHeaders = response.getAllHeaders();
             if(allHeaders != null && allHeaders.length > 0){
                 for(int i = 0 ;i<allHeaders.length ;i++){
                     System.out.println(allHeaders[i].getName()+":"+allHeaders[i].getValue());
                 }
             }
             //获取html代码
//             BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));
//             String tmp = null;
//             while((tmp = reader.readLine())!=null){
//                 System.out.println(tmp);
//             }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }
    }
}
