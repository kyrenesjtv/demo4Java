package me.kyrene.demo.Crawler.JDKCraw;

/**
 * 定义一个抽象类，不然每一个子类都要去实现接口中的方法: 适配器模式
 * Created by wanglin on 2017/11/22.
 */
public  abstract  class AbstractJob  implements  Ijob{

    @Override
    public void beforeRun() {

    }

    @Override
    public void afterRun() {

    }

    @Override
    public void run() {
        this.beforeRun();
        try {
            this.doFetchPage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.afterRun();
    }

    /**
     * 抓取网页的方法
     * @throws Exception
     */
    public abstract void doFetchPage() throws Exception ;

}
