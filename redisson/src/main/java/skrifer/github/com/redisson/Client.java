package skrifer.github.com.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * //公平锁
 * RLock lock1 = redissonClient.getFairLock(KEY_LOCKED);
 * //非公平锁
 * RLock lock1 = redissonClient.getLock(KEY_LOCKED);
 *
 * 公平锁和非公平锁在初次抢占资源时是有区别的。公平锁在初次抢占资源时，会判断CHL队列是否有节点，如果队列上有节点，当前线程一定会在CHL上排队；
 * 非公平锁则不管CHL队列上是否有节点，直接竞争资源，这种方式可能会导致CHL队列上的节点永远获取不到锁，因此称为不公平锁。
 *
 * 可重入锁（ReentrantLock），属于非公平锁。假设当前持有锁的线程是线程A，CHL队列中有线程B、线程C、…、线程N在排队，当线程A再次竞争资源时，可直接获得锁。
 * 也即需要获取锁的线程与当前正在持有锁的线程是同一个时，可以直接获取锁，无须排队。
 *
 * redis中是通过HASH来存储锁的，key是UUID+":"+ThreadId;value 是重入的层数
 * 同一把锁必须由原加锁线程来释放！！！！
 *
 */
@Slf4j
public class Client {
    private static final Long TIME_LOCKED = 2 * 1000l;
    private static final String KEY_LOCKED = "myLock";
    private static RedissonClient redissonClient = null;

    public static void main(String[] args) {
        initRedissonClient();
        lock();
//        autoReleaseLock();

//        tryLock();
    }

    private static void tryLock(){
        for (int i = 0; i < 3; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    log.info(Thread.currentThread().getName() + " \t 运行");
                    RLock lock1 = redissonClient.getLock(KEY_LOCKED);
                    try {
                        // 尝试获取锁
                        boolean b = lock1.tryLock(30, TimeUnit.SECONDS);
                        if (!b) {
                            log.info(Thread.currentThread().getName() + " \t 获取锁失败");
                            return;
                        }
                        log.info(Thread.currentThread().getName() + " \t 获取锁成功");
                    } catch (InterruptedException e) {
                        log.error(Thread.currentThread().getName() + " \t 锁被打断了");
                    }

                    try {
                        // 模拟处理逻辑用时50s
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        log.error(Thread.currentThread().getName() + " \t 业务打断了");
                    }

                    lock1.unlock();
                    log.info(Thread.currentThread().getName() + " \t 释放锁");
                }
            }).start();
        }
    }

    /**
     * 自动释放锁
     */
    private static void autoReleaseLock(){
        RLock lock1 = redissonClient.getLock(KEY_LOCKED);
        log.error("lock1 clas: {}", lock1.getClass());
        // 1s 后自动释放锁
        lock1.lock(1, TimeUnit.SECONDS);

        //尝试获取锁7s, 最多占有锁2s，超过后自动释放，调用unlock可以提前释放。
//        boolean b = lock1.tryLock(7, 2, TimeUnit.SECONDS);
//

        log.info("autoReleaseLock, ThreadName: {} id: {} locked, 重入次数: {}", Thread.currentThread().getName(), Thread.currentThread().getId(), lock1.getHoldCount());
        try {
            Thread.sleep(TIME_LOCKED);
            log.info("autoReleaseLock, ThreadName: {} id: {} locked, 重入次数: {}", Thread.currentThread().getName(), Thread.currentThread().getId(), lock1.getHoldCount());
        } catch (InterruptedException ignore) {
            // ignore
        }
    }

    private static void initRedissonClient() {
        // 1. Create config object
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.8.44:6379");
        // 2. Create Redisson instance
        Client.redissonClient = Redisson.create(config);
    }

    private static void lock() {
        RLock lock1 = redissonClient.getLock(KEY_LOCKED);
        log.error("lock1 clas: {}", lock1.getClass());
        lock1.lock();
        log.info("lock, ThreadName: {} id: {} locked, 重入次数: {}", Thread.currentThread().getName(), Thread.currentThread().getId(), lock1.getHoldCount());

        // 处理业务逻辑
        try {
            Thread.sleep(TIME_LOCKED);
            reLock();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();
            log.info("lock, ThreadName: {} id: {} unlock, 重入次数: {}", Thread.currentThread().getName(), Thread.currentThread().getId(), lock1.getHoldCount());
        }
    }

    /**
     * 测试锁的重入
     */
    private static void reLock() {
        RLock lock1 = redissonClient.getLock(KEY_LOCKED);
        lock1.lock();
        log.info("reLock, ThreadName: {} id: {} locked, 重入次数: {}", Thread.currentThread().getName(), Thread.currentThread().getId(), lock1.getHoldCount());

        // 处理业务逻辑
        try {
            Thread.sleep(TIME_LOCKED);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock1.unlock();
            log.info("reLock, ThreadName: {} id: {} unlock, 重入次数: {}", Thread.currentThread().getName(), Thread.currentThread().getId(), lock1.getHoldCount());
        }
    }
}
