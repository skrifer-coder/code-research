package skrifer.github.com.jvm;

import sun.misc.Contended;

import java.util.concurrent.CountDownLatch;

/**
 * 缓存行相关内容
 */
public class CacheLine {

    static class CacheBody {
        private long p1;
        //用无意义属性做成64字节的隔离墙，防止shuzu两个元素同时被设定在同一个缓存行中，导致cpu在执行线程时，需要频繁的做线程之间得数据同步消耗资源
        private long p2;
        private long p3;
        private long p4;
        private long p5;
        private long p6;
        private long p7;
        private long p8;
        private long p9;

    }

    static int times = 100_000_000;
    static CacheBody c1 = new CacheBody();
    static CacheBody c2 = new CacheBody();
    static CacheBody[] shuzu = {c1, c2};

    static CountDownLatch countDownLatch1 = new CountDownLatch(times);
    static CountDownLatch countDownLatch2 = new CountDownLatch(times);


    public static void main(String[] args) throws InterruptedException {

        long startTime = System.currentTimeMillis();

        Thread thread1 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < times; i++) {
                    shuzu[0].p1 += 1 ;
                    countDownLatch1.countDown();
                }
            }

        };

        Thread thread2 = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < times; i++) {
                    shuzu[1].p1 += 1 ;
                    countDownLatch2.countDown();
                }
            }

        };

        thread1.start();
        thread2.start();

        countDownLatch1.await();
        countDownLatch2.await();


        long endTime = System.currentTimeMillis();

        System.out.println(endTime - startTime);


    }
}
