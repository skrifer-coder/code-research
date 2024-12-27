package skrifer.github.com.springbootredisson;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 如果发送的消息是一个封装的实体类，那该类在不同服务的类路径要一致，否则其他服务接收异常，
 * 大部分情况还是使用String 传递方便，可以使用JSON序列化去过度，示例如下；
 */
@Component
public class TopicMessageHandler {
    private RTopic topic;

    private static final String MESSAGE_TOPIC = "message:topic:1";

    @Value("${spring.application.name:}")
    private String applicationName;

    @Autowired
    private RedissonClient redissonClient;

    @PostConstruct
    public void init() {
        topic = redissonClient.getTopic(MESSAGE_TOPIC);
        topic.addListener(String.class, new MessageListener<String>() {
            @Override
            public void onMessage(CharSequence charSequence, String s) {
//                TopicMessageObj json = JSONObject.parseObject(msg, TopicMessageObj.class);
            }
        });
    }

    /**
     * 发布消息
     * @param message
     */
    public void publishMessage(String message) {
//        TopicMessageObj topicMessageObj = new TopicMessageObj();
//        topicMessageObj.setMessage(message);
//        topicMessageObj.setOrion(applicationName);
//        topic.publish(JSON.toJSONString(topicMessageObj));
    }

    //不建议使用这个，因为需要保证订阅此消息的服务的TopicMessageObj与发布服务完全一致(包扩类路径)
//    public void publishMessage(String message) {
//        TopicMessageObj topicMessageObj = new TopicMessageObj();
//        topicMessageObj.setMessage(message);
//        topicMessageObj.setOrion(applicationName);
//        topic.publish(topicMessageObj);
//    }


}
