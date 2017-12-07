package me.kyrene.demo.thread.manyThread;

/**
 * Created by wanglin on 2017/12/6.
 */
public class MyThread extends Thread {

    private String name;

    public MyThread(String name){
        this.name = name;
    }

    @Override
    public void run() {
        super.run();
        System.out.println("name:"+name+" 子线程ID:"+Thread.currentThread().getId());
    }
}
