package me.kyrene.demo.redis.jedis;

import org.junit.Test;

import java.util.List;

/**
 * Created by wanglin on 2017/11/30.
 */
public class JedisTest {

    @Test
    public void test01(){
        JedisUtil jedisUtil = JedisUtil.getInstance();
        //jedisUtil.set("wanglin","linzai");
//        String wanlin = jedisUtil.get("mok");
//        System.out.println(wanlin);
 //       String wanglin = jedisUtil.getRange("wanglin", 0, 2);
        //String set = jedisUtil.getSet("wanglin", "newGod");
   //     List<String> strings = jedisUtil.mGet("wanglin","pno","w");
//        String s = jedisUtil.setEx("mok", 22, "linzai");
//        Long aLong = jedisUtil.setNx("mok1", "zzz");
//        Long aLong = jedisUtil.setRange("wanglin", 2, "a");
//        Long wanlin = jedisUtil.strLen("wanglin");
        String s = jedisUtil.mSet("wanglin1", "1", "wanglin2", "2");
        System.out.println(s);

    }
}
