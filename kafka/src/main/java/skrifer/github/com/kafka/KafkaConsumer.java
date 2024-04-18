package skrifer.github.com.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "test_topic", groupId = "orderGroup6666")
    public void consumeOrder666(ConsumerRecord<String, String> record, Acknowledgment ack) {
//        log.info("消费订单消息key={},value={}", record.key(), record.value());
        System.out.printf("消费订单消息key=%s,value=%s%n", record.key(), record.value());
        ack.acknowledge();
    }

    @KafkaListener(topics = "test_topic", groupId = "orderGroup")
    public void consumeOrder(ConsumerRecord<String, String> record, Acknowledgment ack) {
//        log.info("消费订单消息key={},value={}", record.key(), record.value());
        System.out.printf("消费订单消息key=%s,value=%s%n", record.key(), record.value());
        ack.acknowledge();
    }
}
