package me.kyrene.demo.jdk8NewFunction.Lambda;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wanglin on 2017/11/6.
 */
public class Lambda {

    String separator = ",";

    @Test
    public void test1() {
        //变成了一个数组
        List<String> strings = Arrays.asList("a", "b", "c");
        //循环打印方式1  这里的e的类型是编译器推算出来的
//        Arrays.asList("a", "b", "c").forEach( e -> System.out.println(e));
        //循环打印方式2 自己给e定义类型
        //  Arrays.asList("a", "b", "c").forEach( (String e ) -> System.out.println(e));
        //如果要做的操作比较多的话,可以加一下花括号
        Arrays.asList("a", "b", "c").forEach((String e) -> {
            System.out.println(e);
            System.out.println("-----------");
        });

    }

    @Test
    public void test2() {
        //引用成员变量,如果成员变量不是final的话,lambda在用的时候会隐形的转为final
        Arrays.asList("a", "b", "c").forEach((String e) -> {
            System.out.println(e + separator);
        });
    }

    @Test
    public void test3() {
        //不知道为什么没有打印出来
        Arrays.asList("a", "b", "c").sort((e1, e2) -> e1.compareTo(e2));

        //sort是没有返回值的，这个是比较和排序,最好是新建一个list
//        Arrays.asList("a", "b", "c").sort((e1,e2) ->{
//            int result = e1.compareTo(e2);
//            return result;
//        });

        //这样子就对了
        List<String> strings = Arrays.asList("a", "c", "b");
        strings.sort((e1, e2) -> e1.compareTo(e2));
    }

    @Test
    public void test4(){
        //functional 本来是常规借口，现在@fFnctionInterface可以能够被lambda表达式使用
        //并且默认方法也是不影响使用的
    }

}
