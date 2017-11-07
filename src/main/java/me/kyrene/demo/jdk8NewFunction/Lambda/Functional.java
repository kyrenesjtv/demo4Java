package me.kyrene.demo.jdk8NewFunction.Lambda;

/**
 * Created by wanglin on 2017/11/7.
 */
@FunctionalInterface
public interface Functional {
    void method();
    default void defaultMethon(){
        System.out.println("aaaa");
    }
}
