package skrifer.github.com.code.lock;
import java.util.concurrent.locks.LockSupport;

/**
 * 用途：park() 方法属于 java.util.concurrent.locks.LockSupport 类，它提供了一种低级别的线程阻塞原语。
 * 它不需要与特定的锁关联，可以在任何地方调用，使得线程阻塞。它通常与 unpark() 方法配对使用，后者可以唤醒一个调用了 park() 的线程。
 *
 * 位置限制：没有位置限制，可以在任何地方调用，不需要先获取锁。
 *
 * 唤醒条件：调用 LockSupport.unpark(Thread thread) 方法可以直接唤醒目标线程，更加灵活和精确。
 * 它可以唤醒一个特定的线程，而无需竞争或不确定性。
 *
 * 线程许可：park() 和 unpark() 是基于每个线程的许可（permit）机制。初始时，每个线程没有许可，
 * 调用 unpark() 会给指定线程添加一个许可，即使之前已经调用过 unpark() 给该线程添加了许可，再调用也不会造成影响（许可不会累积）。
 * 调用 park() 时，如果没有许可，线程会阻塞，如果有许可，则消耗许可并继续执行。
 */
public class ParkUnParkExample {
    public static void main(String[] args) {
        Thread waitingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " 开始等待");
                LockSupport.park(); // 阻塞当前线程
                System.out.println(Thread.currentThread().getName() + " 被唤醒");
            }
        }, "WaitingThread");

        Thread unparkerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000); // 模拟一些工作
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + " 准备唤醒线程");
                LockSupport.unpark(waitingThread); // 唤醒指定线程
            }
        }, "UnparkerThread");

        waitingThread.start();
        unparkerThread.start();
    }
}
