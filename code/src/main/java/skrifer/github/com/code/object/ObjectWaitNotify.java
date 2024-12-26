package skrifer.github.com.code.object;

/**
 * 用途：wait() 方法属于对象监视器（Monitor）的一部分，通常与 synchronized 块或方法一起使用。
 * 当线程调用某个对象的 wait() 方法时，它会释放该对象的锁，并使自己进入等待状态，
 * 直到其他线程调用该对象的 notify() 或 notifyAll() 方法唤醒它，此时线程会重新尝试获取锁并继续执行。
 *
 * 位置限制：只能在同步代码块或同步方法中调用，因为需要先获取到对象的监视器锁。
 *
 * 唤醒条件：可以被 notify() 唤醒，意味着有一个或多个等待线程会被唤醒，但具体哪个线程被唤醒是不确定的；
 * 也可以被 notifyAll() 唤醒，这时所有等待该对象监视器的线程都会进入锁的竞争状态。
 */
public class ObjectWaitNotify {
    public static void main(String[] args) {
        final Object monitor = new Object();

        Thread waitingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (monitor) {
                    System.out.println(Thread.currentThread().getName() + " 开始等待");
                    try {
                        monitor.wait(); // 等待被唤醒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " 被唤醒");
                }
            }
        }, "WaitingThread");

        Thread notifierThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000); // 模拟一些工作
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (monitor) {
                    monitor.notify(); // 唤醒等待的线程
                    System.out.println(Thread.currentThread().getName() + " 发出了唤醒通知");
                }
            }
        }, "NotifierThread");

        waitingThread.start();
        notifierThread.start();
    }
}
