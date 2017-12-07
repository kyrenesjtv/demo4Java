package me.kyrene.demo.thread.manyThread;

import org.junit.Test;

/**
 * Created by wanglin on 2017/12/6.
 */
public class MyRunnableTest {
    @Test
    public void test01(){
        System.out.println("父ID:    "+Thread.currentThread().getId());
        MyRunnable myRunnable = new MyRunnable();
        Thread thread = new Thread(myRunnable);
        thread.start();
    }
}
