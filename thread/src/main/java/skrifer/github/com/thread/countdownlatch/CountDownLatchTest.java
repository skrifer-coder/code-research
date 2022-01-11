package skrifer.github.com.thread.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTest {

    static CountDownLatch countDownLatch = new CountDownLatch(2);

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        Thread t1 = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    countDownLatch.countDown();
                } catch (Exception e) {

                }
                System.out.println("sleep 3000");
            }
        };

        Thread t2 = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(6000);
                    countDownLatch.countDown();
                } catch (Exception e) {

                }
                System.out.println("sleep 6000");
            }
        };


//        Runnable t1 = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(3000);
//                    countDownLatch.countDown();
//                } catch (Exception e) {
//
//                }
//                System.out.println("sleep 3000");
//            }
//        };
//
//        Runnable t2 = new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(6000);
//                    countDownLatch.countDown();
//                } catch (Exception e) {
//
//                }
//                System.out.println("sleep 6000");
//            }
//        };
        try {
//            new Thread(t1).start();
//            new Thread(t2).start();
            t1.start();
            t2.start();
            boolean waitFlag = countDownLatch.await(10, TimeUnit.SECONDS);
            System.out.println("waitFlag:" + waitFlag);

            if (waitFlag == false) {
                if(t1.getState() != Thread.State.TERMINATED){
                    t1.interrupt();
                }
                if(t2.getState() != Thread.State.TERMINATED){
                    t2.interrupt();
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        System.out.println("spent " + (System.currentTimeMillis() - start) / 1000);

    }

}
