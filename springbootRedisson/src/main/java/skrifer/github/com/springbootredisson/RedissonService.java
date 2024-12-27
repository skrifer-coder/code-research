package skrifer.github.com.springbootredisson;

import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class RedissonService {

    private static final Logger log = LoggerFactory.getLogger(RedissonService.class);
    @Autowired
    private RedissonClient redissonClient;

    public String demo() throws InterruptedException {
        RLock lock = redissonClient.getLock("demoLock");
        //尝试获取锁
        boolean b = lock.tryLock(1, 100, TimeUnit.SECONDS);

        if (b) {
            System.out.println("开始执行业务");
            Thread.sleep(2000);
        } else {
            throw new RuntimeException("没拿到锁");
        }

        return "ok";

    }

    /**
     * 分布式锁
     * 底层并非使用redis setnx实现，采用lua脚本搭配看门狗机制实现
     *
     * @throws InterruptedException
     */
    public void lock() throws InterruptedException {
        //阻塞式获取锁
        RLock stopLock = redissonClient.getLock("stopLock");
        stopLock.lock();
        //biz
        stopLock.unlock();

        //非阻塞式获取锁
        RLock nonStopLock = redissonClient.getLock("nonStopLock");
        if (nonStopLock.tryLock(10, TimeUnit.SECONDS)) {
            //Biz
            nonStopLock.unlock();
        } else {
            throw new RuntimeException("没拿到锁");
        }
    }

    /**
     * 发布订阅 (并非redisson特有，其他redis客户端也能实现，如下spring-data-redis)
     * 实现 Redis 消息发布/订阅功能时，可以利用 Spring 的 RedisTemplate 和 MessageListener 接口来实现类似于 Redisson 的功能。
     * <p>1、创建一个消息监听器类，实现 MessageListener 接口；</p>
     * <p>2、使用 RedisMessageListenerContainer 来注册监听器：</p>
     * <p>3、在需要发布消息的地方，可以使用 RedisTemplate 来发布消息：</p>
     * 但比redisson步骤更多，还是优先推荐redisson
     * 封装工具类：TopicMessageHandler
     */
    public void listenerTopic() {
        //订阅方
        {
            RTopic topic = redissonClient.getTopic("myTopic");
            topic.addListener(String.class, new MessageListener<String>() {
                @Override
                public void onMessage(CharSequence charSequence, String s) {
                    System.out.println(s);
                }
            });
        }

        //发布方
        {
            RTopic topic = redissonClient.getTopic("myTopic");
            long b = topic.publish("沃日");
        }

    }

    /**
     * 延迟队列
     * Redisson 提供的延迟队列是一种基于 Redis 的数据结构，允许用户将任务或消息放入队列中，并在指定的延迟时间后执行。
     * 这种机制非常适合需要延迟处理的场景，比如定时任务、消息重试、延迟通知等。
     * <p>
     * Redisson 的延迟队列基于 Redis 的 Sorted Set（有序集合）实现，使用时间戳作为排序依据。
     * 用户可以将任务添加到队列中，并指定延迟时间，Redisson 会在适当的时间将任务取出并执行。
     * 原理：
     * 存储结构: 延迟队列中的每个任务都以 (延迟时间, 任务内容) 的形式存储在 Sorted Set 中。延迟时间作为 Sorted Set 的分数，任务内容作为 Sorted Set 的成员。
     * 延迟机制: Redisson 使用 Redis 的 ZADD 命令添加任务到 Sorted Set，并设置延迟时间作为分数。Redis 会根据分数对任务进行排序，延迟时间越短的任务排在越前面。
     * 任务获取: Redisson 使用 Redis 的 ZRANGEBYSCORE 命令获取延迟时间已经到期的任务。具体来说，它会获取分数小于等于当前时间戳的任务。
     * 任务处理: Redisson 会将获取到的任务从 Sorted Set 中删除，并将其添加到普通队列中，供消费者进行处理。
     */
    public void delayedQueue() {
        // 普通队列
        RQueue<String> queue = redissonClient.getQueue("normalQueue");

        //延迟队列
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(queue);

        delayedQueue.offer("task5", 5, TimeUnit.SECONDS);
        delayedQueue.offer("task10", 10, TimeUnit.SECONDS);

        while (true) {
            String poll = queue.poll();
            if (poll != null) {
                System.out.println(System.currentTimeMillis() + ":" + poll);
            }

            if (delayedQueue.isEmpty()) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Redisson 和 RedisTemplate 都提供了事务操作，但 Redisson 的事务功能更加强大，支持异步操作、嵌套和回滚。
     * 选择哪种方式取决于您的具体需求。
     *
     * 使用 RedisTemplate 时，事务的实现方式略有不同。RedisTemplate 使用的是 Redis 的 MULTI/EXEC 命令来实现事务。
     */
    public void transcation(){
        RTransaction transaction = redissonClient.createTransaction(TransactionOptions.defaults());

        try {
            RMap<String, String> map = transaction.getMap("map");
            map.put("k1", "v1");
//            int i = 1 / 0;
            map.put("k2", "v2");
            transaction.commit();
        } catch (Exception e){
            e.printStackTrace();
            transaction.rollback();
        }

        System.out.println(redissonClient.getMap("map").get("k1"));
        System.out.println(redissonClient.getMap("map").get("k2"));
    }

    private final AtomicLong count = new AtomicLong(0);
    /**
     * 并发计数器：例如网站访问计数、商品库存计数等，保证在高并发下计数的准确性。
     * 限流控制：在微服务架构中，对API调用进行频率限制，防止系统过载。
     * 分布式锁：虽然RAtomicLong本身不直接提供锁功能，但可以结合其他机制（如乐观锁）实现简单的分布式锁。
     * 统计分析：例如实时统计在线用户数量或活跃用户数。
     * 单机场景下AtomicLong与RAtomicLong功能一致，但是RAtomicLong支持分布式
     */
    public void atomicLong(){
        RAtomicLong rtomicLong = redissonClient.getAtomicLong("RtomicLong");
        //初始化计数器
        rtomicLong.set(0L);

        for (int i = 0; i < 100; i++) {
            new Thread(rtomicLong::incrementAndGet).start();
            new Thread(count::incrementAndGet).start();
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("count=" + count.get());
        System.out.println("rtomicLong=" + rtomicLong.get());

    }
}
