package skrifer.github.com.kafka;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

//@Component
public class KafkaProducer implements ApplicationRunner {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, Object> datas = new HashMap<>();
        datas.put("address", "成都市高新区" + System.currentTimeMillis());
        kafkaTemplate.send("test_topic", "order:", new Gson().toJson(datas));
    }
}
