package io.zaplink.notification.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import io.zaplink.notification.dto.request.EmailRequest;
import io.zaplink.notification.handler.RegistreationEmailHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class KafkaConsumer
{
    private final RegistreationEmailHandler registreationEmailHandler;

    @KafkaListener(topics = "topic-2", groupId = "notification-group")
    void listener1(EmailRequest data)
    {
        log.info("Received message: {}", data);
        registreationEmailHandler.sendVerificationEmail(data);
    }
}
