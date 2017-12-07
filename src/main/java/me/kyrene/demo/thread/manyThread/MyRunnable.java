package me.kyrene.demo.thread.manyThread;

/**
 * Created by wanglin on 2017/12/6.
 */
public class MyRunnable implements Runnable {

    public MyRunnable(){

    }

    @Override
    public void run() {
        System.out.println("子线程ID"+Thread.currentThread().getId());
    }
}
