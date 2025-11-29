package io.zaplink.processor.service.helper;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import io.zaplink.processor.dto.request.UrlConsumerRequest;

@Service
public class KafkaListenerHelper {
    @KafkaListener(topics = "topic-1", groupId = "zaplink-consumer-group")
    void listener1(UrlConsumerRequest data) {
        System.out.println("Listener Data: " + data);
    }
}
