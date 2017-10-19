package me.kyrene.demo.proxy.cglibproxy;

/**
 * Created by wanglin on 2017/10/19.
 */
public class Test {
    public static  void main(String[] args){
        RemoveBook removeBook = new RemoveBook();
        RemoveBookProxy removeBookProxy = new RemoveBookProxy();
        RemoveBook instance = (RemoveBook) removeBookProxy.instance(removeBook);
        instance.removeBook();
    }
}
/**
 * 动态代理类的字节码在程序运行时由Java反射机制动态生成，无需程序员手工编写它的源代码。
 * 动态代理类不仅简化了编程工作，而且提高了软件系统的可扩展性，因为Java 反射机制可以生成任意类型的动态代理类。
 * java.lang.reflect 包中的Proxy类和InvocationHandler 接口提供了生成动态代理类的能力。
 * cglib代理需要有第三方库
 * 是针对类的
 * 要实现MethodInterceptor
 */