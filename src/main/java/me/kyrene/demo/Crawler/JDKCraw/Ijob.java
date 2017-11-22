package me.kyrene.demo.Crawler.JDKCraw;

/**
 * Created by wanglin on 2017/11/22.
 */
public interface Ijob extends Runnable {
    /**
     * 在JOB执行之前执行回调的方法
     */
    void beforeRun();

    /**
     * 在JOB执行之后执行回调的方法
     */
    void  afterRun();
}
