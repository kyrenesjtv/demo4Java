package me.kyrene.demo.thread.manyThread;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.*;

/**
 * Created by wanglin on 2017/12/6.
 */
public class MyCallableTest {

    @Test
    public void test01() throws ExecutionException, InterruptedException {
        System.out.println("-----程序开始-----");
        Date date1 = new Date();
        int taskSize = 5;
        //创建一个线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(taskSize);
        //创建有多个返回值的任务
        ArrayList<Future> list = new ArrayList<>();
        for(int i = 0 ;  i<taskSize;i++){
            Callable myCallable = new MyCallable(i + "");
            //执行任务并获取future对象
            Future future = threadPool.submit(myCallable);
            list.add(future);
        }
        //关闭线程池
        threadPool.shutdown();
        // 获取所有并发任务的运行结果
        for (Future f : list) {
            // 从Future对象上获取任务的返回值，并输出到控制台
            System.out.println(">>>" + f.get().toString());
        }

        Date date2 = new Date();
        System.out.println("----程序结束运行----，程序运行时间【"
                + (date2.getTime() - date1.getTime()) + "毫秒】");
    }
}
