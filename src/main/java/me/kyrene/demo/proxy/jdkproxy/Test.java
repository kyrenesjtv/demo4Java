package me.kyrene.demo.proxy.jdkproxy;

/**
 * Created by wanglin on 2017/10/19.
 */
public class Test {
    public static  void main(String[] args){
        BookFacadeProxy bookFacadeProxy = new BookFacadeProxy();
        BookFacade bookFacade = new BookFacadeImpl();
        BookFacade o = (BookFacade) bookFacadeProxy.newInstance(bookFacade);
        o.addBook();


        /**
         * JDK代理是不需要以来第三方的库，只要要JDK环境就可以进行代理，它有几个要求
         * 实现InvocationHandler
         * 使用Proxy.newProxyInstance产生代理对象
         * 被代理的对象必须要实现接口
         */
    }
}
