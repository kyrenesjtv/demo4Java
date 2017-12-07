package me.kyrene.demo.thread.manyThread;

import java.util.Date;
import java.util.concurrent.Callable;

/**
 * Created by wanglin on 2017/12/6.
 */
public class MyCallable implements Callable{

    private String taskNum;

    public MyCallable(String taskNum){
        this.taskNum=taskNum;
    }

    @Override
    public Object call() throws Exception {
        System.out.println(">>>"+ taskNum +"<<      任务启动");
        Date date = new Date();
        Thread.sleep(1000);
        Date date1 = new Date();
        long time = date1.getTime()-date.getTime();
        System.out.println(">>>>"+taskNum+"<<<< 任务结束");
        return taskNum+"任务返回运行结果，耗时"+time+"毫秒";
    }
}
