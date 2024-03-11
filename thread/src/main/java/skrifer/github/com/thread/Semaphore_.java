package skrifer.github.com.thread;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore是Java并发类库中的一个工具类，可以用于控制同时访问某个资源的线程数量。
 * Semaphore内部维护了一定数量的许可证，线程可以通过acquire()方法获取许可证，使用完之后再通过release()方法释放许可证。
 * 当没有许可证可用时，acquire()方法会被阻塞，直到有其他线程释放许可证
 */
public class Semaphore_ {

    private Semaphore semaphore = new Semaphore(100); // 初始化信号量，设置最大并发数为100

    public void processRequest() {
        try {
            semaphore.acquire(); // 尝试获取许可证，如果没有许可证可用，线程会被阻塞
            // 执行接口逻辑
            // ...
            TimeUnit.SECONDS.sleep(1); // 模拟接口耗时1秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release(); // 释放许可证
        }
    }
}
