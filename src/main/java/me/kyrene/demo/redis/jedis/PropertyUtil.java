package me.kyrene.demo.redis.jedis;

import redis.clients.jedis.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by wanglin on 2017/11/29.
 */
public class PropertyUtil {

    //加载property文件到io流里面
    public static Properties loadProperties(String propertyFile) {
        Properties properties = new Properties();
        try {
            InputStream is = PropertyUtil.class.getClassLoader().getResourceAsStream(propertyFile);
            if(is == null){
                is = PropertyUtil.class.getClassLoader().getResourceAsStream(propertyFile);
            }
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
