package io.zaplink.zaplink_consumer_service.service.impl;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import io.zaplink.zaplink_consumer_service.dto.request.ShortUrlConsumerRequest;

@Service
public class KafkaListeners {
    @KafkaListener(topics = "topic-1", groupId = "zaplink-group")
    void listener1(ShortUrlConsumerRequest data) {
        System.out.println("Listener Data: " + data);
    }
}
