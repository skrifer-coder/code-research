package skrifer.github.com.thread;

public class JoinYieldWaitTest {
    static boolean flag = false;
    public static void main(String[] args) throws Exception {


        Thread thread = new Thread() {
            @Override
            public void run() {
                while (flag == false) {
                    Thread.yield();
                }
                System.out.println("thread end");
            }
        };

        Thread thread2 = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        this.notify();
                    }
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread2 end");
            }
        };

        thread.start();
        flag = true;
        thread.join();

//        thread2.start();
        thread2.start();
        synchronized (thread2){
            thread2.wait();
        }





        System.out.println("main thread end");



    }
}
