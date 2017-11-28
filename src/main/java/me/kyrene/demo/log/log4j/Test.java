package me.kyrene.demo.log.log4j;

/**
 * Created by wanglin on 2017/11/27.
 */
public class Test {
    @org.junit.Test
    public void test01(){
        String debug = "debug message:XXXXXXXXXXXX";
        //具体要看你log4j.properties的设置的级别
        log4jUtilsSimple.info(debug);
    }
}
