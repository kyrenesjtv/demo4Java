package me.kyrene.demo.jdk8NewFunction.classlib;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by wanglin on 2017/11/16.
 */
public class ClassLibFunction {

    @Test
    //optional 解决空指针异常？
    public void test1() {
        Optional<Object> fullname = Optional.ofNullable(null);
        //是否有值 有 true  没有 false
        System.out.println("Fullname is set ? " + fullname.isPresent());
        //有值打印值，没有值打印自己定义的 return value != null ? value : other.get();
        System.out.println("fullname :" + fullname.orElseGet(() -> "[none]"));
        System.out.println("fullname :" + fullname.orElse("[none]"));
        //
        System.out.println(fullname.map(s -> "hey" + s + "!").orElse("Hey Stranger"));
    }

    @Test
    public void test2() {
        Optional<String> fullname = Optional.ofNullable("Tom");
        System.out.println("fullname is set? " + fullname.isPresent());
        System.out.println("fullname:" + fullname.orElseGet(() -> "none"));
        System.out.println("fullname:" + fullname.map(s -> "hey" + s + "!").orElse("Hey Stranger"));
    }

    @Test
    //Stream 过滤
    public void test3() {

        List<Streams.Task> tasks = Arrays.asList(new Streams.Task(Streams.Status.OPEN, 5), new Streams.Task(Streams.Status.OPEN, 13), new Streams.Task(Streams.Status.CLOSE, 8));
        //得到状态是open的
        int sum = tasks.stream().filter(
                task -> task.getStatus() == Streams.Status.OPEN).mapToInt(Streams.Task::getPoints).sum();
        System.out.println("Total Sum1:  " + sum);
        //得到所有的总数
        double totalPoints = tasks.stream().parallel().map(task -> task.getPoints()).reduce(0, Integer::sum);
        System.out.println("Total AllSum2: " + totalPoints);
        //根据状态进行分类  collectors 工具类 util.stream下面的
        Map<Streams.Status, List<Streams.Task>> map = tasks.stream().collect(Collectors.groupingBy(Streams.Task::getStatus));
        System.out.println("map:"+map);
        //结果占比
        List<String> collect = tasks.stream().mapToInt(Streams.Task::getPoints).asLongStream().mapToDouble(points -> points / totalPoints).boxed().mapToLong(weigth -> (long) (weigth * 100)).mapToObj(percentage -> percentage + "%").collect(Collectors.toList());
        System.out.println("collect:"+collect);
        //读取文件
        String filename="";
        Path path = new File(filename).toPath();
        try {
            Files.lines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
