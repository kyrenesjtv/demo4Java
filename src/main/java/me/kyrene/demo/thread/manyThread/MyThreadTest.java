package me.kyrene.demo.thread.manyThread;

import org.junit.Test;

/**
 * Created by wanglin on 2017/12/6.
 */
public class MyThreadTest {
    
    @Test
    public void test01(){
        System.out.println("主线程ID:"+Thread.currentThread().getId());
        MyThread thread1 = new MyThread("thread1");
        //start的话是会创造一个新的线程
        thread1.start();
        MyThread thread2 = new MyThread("thread2");
        //如果是run方法的话，并不会创造一个线程
        thread2.run();
    }
}
