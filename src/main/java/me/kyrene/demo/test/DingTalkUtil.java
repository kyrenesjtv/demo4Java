package me.kyrene.demo.test;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: RZPUBCAR_APPSRV
 * @Author: wanglin
 * @CreateDate: 2018/9/7 10:13
 */
public class DingTalkUtil {


    public static String getAccessToken() throws IOException, URISyntaxException {
        HashMap<String, String> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("uri","https://59.202.29.46:8004/oapi/getToken");
        stringObjectHashMap.put("appid", "dtdream_shengjiguanshiwuguanliju");
        stringObjectHashMap.put("appsecret", "bDIR2FRXY8JZ");
        CloseableHttpResponse excute = HttpClientUtil.excute(stringObjectHashMap);
        JSONObject jsonString = getJsonString(excute);
        String retMessage = jsonString.get("retMessage").toString();
        String token="";
        if("success".equals(retMessage)){
            Map<String,String> retData = (Map<String, String>) jsonString.get("retData");
             token = retData.get("token");
        }
        return token;

    }

    public void sendDingTalkMessage() throws IOException, URISyntaxException {
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        String accessToken = getAccessToken();
        String uri = "https://59.202.29.46:8004/oapi/message/send?access_token="+accessToken;

        HttpClientUtil.excutePost(uri,formparams);
    }

    private static JSONObject getJsonString(CloseableHttpResponse excute) throws IOException {
        HttpEntity entity = excute.getEntity();
        InputStream is = entity.getContent();
        //读取输入流，即返回文本内容
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        String line = "";
        while((line=br.readLine())!=null){
            sb.append(line);
        }
        JSONObject jsonObject = JSONObject.parseObject(sb.toString());
        return jsonObject;
    }
}
