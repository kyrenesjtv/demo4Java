package me.kyrene.demo.thread.manyThread;

import org.junit.Test;

import java.time.Clock;

/**
 * Created by wanglin on 2017/12/7.
 */
public class YeildTest {


    @Test
    public void test01(){
        YeildTest yeildTest = new YeildTest();
        MyThread myThread = yeildTest.new MyThread();
        myThread.start();
    }
    class MyThread extends Thread{
        @Override
        public void run() {
            long beginTime = Clock.systemUTC().millis();
            System.out.println("start");
            int count = 0;
            for(int i = 0 ;i<50000000;i++){
                count=count+(i+1);
                Thread.yield();
            }
            long endTime = Clock.systemUTC().millis();
            System.out.println("end");
            System.out.println("耗时"+(endTime-beginTime)+"毫秒");
        }
    }
}
