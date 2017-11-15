package me.kyrene.demo.jdk8NewFunction.Lambda;

import java.util.function.Supplier;

/**
 * Created by wanglin on 2017/11/15.
 */
public interface FunctionalFactory {
    static Functional create(Supplier<Functional> supplier){
        return  supplier.get();
    }
}
