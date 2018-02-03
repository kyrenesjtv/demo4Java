package me.kyrene.demo.lock.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wanglin on 2018/2/3.
 */
public class casTest {
//      static int j = 0;
//
//    public static void main(String args[]) {
//        Thread[] th = new Thread[100];
//        for (int i = 0; i < th.length; i++) {
//            th[i] = new Thread() {
//
//                @Override
//                public void run() {
//                    for (int i = 0; i < 10000; i++) {
//                        j++;
//                    }
//                }
//            };
//            th[i].start();
//        }
//
//        for (Thread thread : th) {
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        /**
//         * 未对j进行cas操作,j的值总是小于10000000
//         *
//         */
//        System.out.println(j);
//    }
/********************************************************************************************/
     private static AtomicInteger j = new AtomicInteger(0);//还有其他种类,

    public static void main(String args[]) {
        Thread[] th = new Thread[100];
        for (int i = 0; i < th.length; i++) {
            th[i] = new Thread() {

                @Override
                public void run() {
                    for (int i = 0; i < 10000; i++) {
                        j.incrementAndGet();
                    }
                }
            };
            th[i].start();
        }

        for (Thread thread : th) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        /**
         * 对j进行cas操作,j的值总是等于10000000
         *
         */
        System.out.println(j);
    }
}
