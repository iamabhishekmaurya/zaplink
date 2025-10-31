package io.zaplink.zaplink_producer_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.zaplink.zaplink_producer_service.dto.request.ShortUrlConsumerRequest;

@Service
public class KafkaService
{
    private static Logger                       logger = LoggerFactory.getLogger( KafkaService.class );
    private static final String                 TOPIC  = "topic-1";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public KafkaService( KafkaTemplate<String, Object> kafkaTemplate )
    {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage( ShortUrlConsumerRequest message )
    {
        kafkaTemplate.send( TOPIC, message );
        logger.info( "✅ Sent message: {}", message );
    }
}
