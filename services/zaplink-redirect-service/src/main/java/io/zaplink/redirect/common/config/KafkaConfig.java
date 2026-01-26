package io.zaplink.redirect.common.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

/**
 * Kafka producer configuration for analytics events.
 * 
 * Uses JacksonJsonSerializer (Spring Kafka 4.0+) with type headers enabled
 * for proper polymorphic deserialization in the consumer service.
 */
@Configuration
public class KafkaConfig
{
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Bean
    public ProducerFactory<String, Object> producerFactory()
    {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put( ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers );
        configProps.put( ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class );
        configProps.put( ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class );
        configProps.put( ProducerConfig.ACKS_CONFIG, "all" );
        configProps.put( ProducerConfig.RETRIES_CONFIG, 3 );
        configProps.put( ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true );
        configProps.put( ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5 );
        // Configure type mappings via Kafka properties (Spring Kafka 4.0 way)
        // The token (e.g., "urlClickEvent") is sent in the __TypeId__ header
        // The consumer service maps the same token to its local class
        configProps.put( JacksonJsonSerializer.ADD_TYPE_INFO_HEADERS, true );
        configProps
                .put( JacksonJsonSerializer.TYPE_MAPPINGS,
                      "urlClickEvent:io.zaplink.redirect.dto.event.UrlClickEvent,qrScanEvent:io.zaplink.redirect.dto.event.QrScanEvent" );
        return new DefaultKafkaProducerFactory<>( configProps );
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate( ProducerFactory<String, Object> producerFactory )
    {
        return new KafkaTemplate<>( producerFactory );
    }
}
