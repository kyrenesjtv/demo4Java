package me.kyrene.demo.jdk8NewFunction.Lambda;

/**
 * Created by wanglin on 2017/11/7.
 */
@FunctionalInterface
public interface Functional {

    void method();

    /**
     * 默认方法不一定要被实现
     */
    default void defaultMethon(){
        System.out.println("default string");
    }
}
