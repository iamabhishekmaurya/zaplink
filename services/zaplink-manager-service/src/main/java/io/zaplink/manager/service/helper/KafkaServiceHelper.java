package io.zaplink.manager.service.helper;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import io.zaplink.manager.dto.request.AnalyticsEvent;
import io.zaplink.manager.dto.request.ClickCount;
import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class KafkaServiceHelper
{
    private static final String                 CLICKCOUNT_TOPIC = "url-clickcount-topic";
    private static final String                 ANALYTICS_TOPIC  = "url-analytics-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;
    public KafkaServiceHelper( KafkaTemplate<String, Object> kafkaTemplate )
    {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage( ClickCount clickCount )
    {
        kafkaTemplate.send( CLICKCOUNT_TOPIC, clickCount );
        log.info( "✅ ClickCount sent pushed to Kafka: {}", clickCount );
    }

    public void sendMessage( AnalyticsEvent analyticsEvent )
    {
        kafkaTemplate.send( ANALYTICS_TOPIC, analyticsEvent );
        log.info( "✅ AnalyticsEvent sent pushed to Kafka: {}", analyticsEvent );
    }
}
