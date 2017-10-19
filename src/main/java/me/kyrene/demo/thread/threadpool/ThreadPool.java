package me.kyrene.demo.thread.threadpool;

import java.util.LinkedList;
import java.util.List;

/**
 * 线程池类，线程管理器：创建线程，执行任务，销毁线程，获取线程基本信息
 * Created by wanglin on 2017/10/19.
 */
public class ThreadPool {

    //线程池中默认的线程是5个
    private static int worker_num = 5;
    //工作线程
    private WorkThread[] workThreads;
    //未处理的任务
    private static volatile int finished_task = 0;
    //任务队列，作为一个缓冲，arraylist线程不安全
    private List<Runnable> taskQueue = new LinkedList<Runnable>();
    private static ThreadPool threadPool;

    /**
     * 创建具有默认线程的个数
     */
    private ThreadPool() {
        this(5);
    }

    /**
     * 创建线程池，可以自定义线程池的大小
     *
     * @param worker_num
     */
    private ThreadPool(int worker_num) {
        ThreadPool.worker_num = worker_num;
        workThreads = new WorkThread[worker_num];
        for (int i = 0; i < worker_num; i++) {
            workThreads[i] = new WorkThread();
            workThreads[i].start();//开启线程池中的线程
        }
    }

    /**
     * 单态模式，获得一个默认个数的线程池
     *
     * @return
     */
    public static ThreadPool getThreadPool() {
        return getThreadPoll(ThreadPool.worker_num);
    }

    /**
     * 单态模式 获得一个指定线程个数的线程池
     *
     * @param worker_num1
     * @return
     */
    public static ThreadPool getThreadPoll(int worker_num1) {
        if (worker_num1 <= 0) {
            worker_num1 = ThreadPool.worker_num;
        }
        if (threadPool == null) {
            threadPool = new ThreadPool(worker_num1);
        }
        return threadPool;
    }

    /**
     * 执行任务，其实只是把任务加入任务队列，什么时候执行由线程池管理器决定
     *
     * @param task
     */
    public void execute(Runnable task) {
        synchronized (taskQueue) {
            taskQueue.add(task);
            taskQueue.notify();
        }
    }

    /**
     * 批量执行任务，其实只是把任务加入任务队列，什么时候执行由线程池管理器决定
     *
     * @param task
     */
    public void execute(Runnable[] task) {
        synchronized (taskQueue) {
            for (Runnable t : task) {
                taskQueue.add(t);
                taskQueue.notify();
            }
        }
    }

    /**
     * * 批量执行任务，其实只是把任务加入任务队列，什么时候执行由线程池管理器决定
     *
     * @param task
     */
    public void excute(List<Runnable> task) {
        synchronized (taskQueue) {
            for (Runnable t : task) {
                taskQueue.add(t);
                taskQueue.notify();
            }
        }
    }

    /**
     * 销毁线程，该方法保证在所有任务都完成的情况下才销毁所有线程，否则等任务完成才销毁
     */
    public void destory() {
        //任务还没执行完
        while (!taskQueue.isEmpty()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //工作线程停止工作 并且 置为null
        for (int i = 0; i < worker_num; i++) {
            workThreads[i].stopWorker();
            workThreads[i] = null;
        }

        threadPool = null;
        taskQueue.clear();//清空队列
    }


    /**
     * 返回已完成任务的个数,这里的已完成是只出了任务队列的任务个数，可能该任务并没有实际执行完成
     *
     * @return
     */
    public int getFinishedTasknumber() {
        return finished_task;
    }

    /**
     * 返回工作线程的个数
     *
     * @return
     */
    public int getWorkTasknumber() {
        return worker_num;
    }

    /**
     * 返回任务队列的长度，即还没完成的任务个数
     *
     * @return
     */
    public int getWaitTasknumber() {
        return taskQueue.size();
    }

    /**
     * 重写toString方法，返回线程池信息:工作线程个数和已完成任务个数
     *
     * @return
     */
    @Override
    public String toString() {
        return "WorkThradNumber：" + worker_num + "。finished task number：" + finished_task + "。wait task number：" + getWaitTasknumber();
    }

    /**
     * 内部类，工作线程
     */
    private class WorkThread extends Thread {
        //该工作线程是否有效，用于结束该工作线程
        private boolean isRunning = true;

        /**
         *关键所在，如果任务队列不为空，则取出任务执行，若任务队列为空则等待
         */
        @Override
        public void run() {
            Runnable r = null;
            while(isRunning){
                synchronized (taskQueue){
                    //队列为空
                    while (isRunning && taskQueue.isEmpty()){
                        try {
                            taskQueue.wait(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if(!taskQueue.isEmpty()){
                        //取出第一个
                        r=taskQueue.remove(0);
                    }
                }
                if(r != null ){
                    r.run();//执行任务
                }
                finished_task++;
                r=null;
            }
        }

        public void stopWorker() {
            isRunning = false;
        }
    }
}
