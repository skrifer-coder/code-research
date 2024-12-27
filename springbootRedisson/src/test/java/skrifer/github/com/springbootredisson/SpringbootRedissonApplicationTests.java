package skrifer.github.com.springbootredisson;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootRedissonApplicationTests {

    @Autowired
    private RedissonService redissonService;

    @Test
    void contextLoads() {
        try {
//            redissonService.demo();
//            redissonService.lock();
//            redissonService.listenerTopic();
//            redissonService.delayedQueue();
//            redissonService.transcation();
//            redissonService.atomicLong();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
