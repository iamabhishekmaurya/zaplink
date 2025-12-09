package io.zaplink.auth.service.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.zaplink.auth.dto.request.EmailRequest;

@Service
public class KafkaServiceHelper
{
    private static Logger                       logger = LoggerFactory.getLogger( KafkaServiceHelper.class );
    private static final String                 TOPIC  = "topic-2";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public KafkaServiceHelper( KafkaTemplate<String, Object> kafkaTemplate )
    {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage( EmailRequest message )
    {
        kafkaTemplate.send( TOPIC, message );
        logger.info( "✅ Sent message: {}", message );
    }
}
