package me.kyrene.demo.proxy.jdkproxy;

/**
 * Created by wanglin on 2017/10/19.
 */
public class BookFacadeImpl implements  BookFacade{

    public void addBook() {
        System.out.println("add book now ");
    }
}
