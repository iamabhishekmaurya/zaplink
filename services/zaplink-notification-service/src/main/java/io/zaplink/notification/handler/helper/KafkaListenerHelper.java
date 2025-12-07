package io.zaplink.notification.handler.helper;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import io.zaplink.notification.dto.request.EmailRequest;

@Service
public class KafkaListenerHelper
{
    @KafkaListener(topics = "topic-email-notification", groupId = "zaplink-consumer-group")
    void listener1( EmailRequest data )
    {
        System.out.println( "Listener Data: " + data );
    }
}
