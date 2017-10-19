package me.kyrene.demo.proxy.jdkproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by wanglin on 2017/10/19.
 */
public class BookFacadeProxy implements InvocationHandler{
    //被代理类对象
    private Object target;

    public Object newInstance(Object o){
        this.target=o;
        //取得代理对象
        return  Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        System.out.println("before ---------");
         result = method.invoke(target, args);
        System.out.println("after ------------");
        return result;
    }
}
