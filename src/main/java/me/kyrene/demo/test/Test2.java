package me.kyrene.demo.test;

import me.kyrene.demo.thread.manyThread.MyCallable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.*;

/**
 * @ProjectName: demo4Java
 * @Author: AlbertW
 * @CreateDate: 2019/2/22 10:42
 */
public class Test2 {

    @org.junit.Test
    public void test01() throws IOException, URISyntaxException {

        Map<String, Object> stringObjectMap = new HashMap<String, Object>();
        stringObjectMap.put("111",11);
        int o = (int) stringObjectMap.get("111");
        System.out.println(o);

    }

    @org.junit.Test
    public void test02() throws IOException, URISyntaxException {

        String test = "杭州市";
        String substring = test.substring(2,test.length());
        System.out.println(substring);

    }


    /**
     *  插入排序
     * @param a 数组
     * @param n 数组长度
     */
    public static void insertionSort(int[] a , int n )  {
        if(n <= 0 ){
            return;
        }
        for(int i = 1 ; i < n ; i++){
            int value = a[i];
            int j=i-1;
            for(; j>=0;j--){
                //进行数据交换
                if(a[j] > value){
                    a[j+1] = a[j];
                }else {
                    break;
                }
            }
            //插入数据
            a[j+1]=value;
        }
    }

    @org.junit.Test
    public void test03() throws IOException, URISyntaxException {
//        System.out.println("111");
        int[] a = new int[]{4,5,6,1,3,2};
        System.out.println(a.toString());
        insertionSort(a,a.length);
        System.out.println(a.toString());
    }

    /**
     *  选择排序
     * @param a 数组
     * @param n 数组长度
     */
    public static void selectionSort(int[] a , int n){
        if(n < 0 ){
            return;
        }
        for(int i = 0 ; i < n-1 ; i++){
            int k = i;

            for(int j = k +1 ;j<n;j++){
                //找到最小位置的索引
                if(a[j]<a[k]){
                    k=j;
                }
            }
            //数据交换
            if(i != k ){
                int temp = a[i];
                a[i]= a[k];
                a[k]=temp;
            }
        }
    }

    @org.junit.Test
    public void test04() throws IOException, URISyntaxException {
        //        System.out.println("111");
        int[] a = new int[]{4,5,6,1,3,2};
        System.out.println(a.toString());
        selectionSort(a,a.length);
        System.out.println(a.toString());
    }

    @org.junit.Test
    public void test05() throws IOException, URISyntaxException {
        Map<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("result",true);
        Boolean result = (Boolean) stringObjectHashMap.get("result");
        System.out.println("11111");



    }



    @org.junit.Test
    public void test06() throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        System.out.println("----程序开始运行----");
        Date date1 = new Date();
        List<Integer> integers = new ArrayList<>();

        int taskSize = 5;
        // 创建一个线程池
//        ExecutorService pool = Executors.newFixedThreadPool(taskSize);
        ExecutorService pool = new ThreadPoolExecutor(taskSize,taskSize,0L, TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
        // 创建多个有返回值的任务
        List<Future> list = new ArrayList<Future>();
        for (int i = 0; i < taskSize; i++) {
//            Callable c = new MyCallable(i + " ");
            // 执行任务并获取Future对象
            Future f = pool.submit(new Callable<String>() {

                @Override
                public String call() throws Exception {
                    integers.add(1);
                    return "wanglin";
                }
            });
            list.add(f);
        }
        // 关闭线程池
        pool.shutdown();

        // 获取所有并发任务的运行结果
        for (Future f : list) {
            // 从Future对象上获取任务的返回值，并输出到控制台
            System.out.println(">>>" + f.get().toString()+"yunxing");
        }
        System.out.println(integers.toString());
        Date date2 = new Date();
        System.out.println("----程序结束运行----，程序运行时间【"
                + (date2.getTime() - date1.getTime()) + "毫秒】");
    }

    @org.junit.Test
    public void test07() throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        int taskSize = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(taskSize);
        Date date1 = new Date();
        for(int i = 0 ; i< taskSize ; i++){
            final int finalI = i;
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("第"+finalI+"个正在执行");
                }
            });
        }
        // 关闭线程池 shutdownnow是立即关闭 后面的也不执行
        executorService.shutdown();
        Date date2 = new Date();
        System.out.println("----程序结束运行----，程序运行时间【"
                + (date2.getTime() - date1.getTime()) + "毫秒】");
    }


    @org.junit.Test
    public void test08() throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        String s = UUID.randomUUID().toString();
        System.out.println(s.replaceAll("-","").length());
        System.out.println(s.replaceAll("-",""));
    }

    @org.junit.Test
    public void test09() throws IOException, URISyntaxException, ExecutionException, InterruptedException {
        String aa = null;
        if(aa == null){
            System.out.println("aaa");
        }

    }
    @org.junit.Test
    public void test10() throws IOException, URISyntaxException, ExecutionException, InterruptedException {
         List<User> users = Arrays.asList( new User("jack",17,10),
                 new User("jack",10,10),
                 new User("jack",19,11),
                 new User("apple",25,15),
                 new User("tommy",23,8),
                 new User("jessica",15,13));

        users.sort((o1,o2) -> o1.getAge()-o2.getAge());
        System.out.println(users.toString());
    }
}
