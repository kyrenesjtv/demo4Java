package me.kyrene.demo.thread.manyThread;

import org.junit.Test;

/**
 * Created by wanglin on 2017/12/7.
 */
public class SleepTest {
    private int i = 0;
    private Object object = new Object();

    @Test
    public void  test01(){
        SleepTest sleepTest = new SleepTest();
        MyThread myThread1 = sleepTest.new MyThread();
        MyThread myThread2 = sleepTest.new MyThread();
        myThread1.start();
        myThread2.start();
    }
    class MyThread extends Thread{
        @Override
        public void run() {
            synchronized (object){
                i++;
                System.out.println("i:  "+i);
                try {
                    System.out.println("线程:"+Thread.currentThread().getName()+"进入睡眠");
                    Thread.currentThread().sleep(1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                System.out.println("线程:"+Thread.currentThread().getName()+"睡眠结束");

            }
        }
    }
}
