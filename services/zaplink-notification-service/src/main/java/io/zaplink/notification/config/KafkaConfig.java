package io.zaplink.notification.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import io.zaplink.notification.dto.request.EmailRequest;

@Configuration
public class KafkaConfig
{
    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;
    @Value("${spring.kafka.consumer.group-id:notification-group}")
    private String groupId;
    @Bean
    public Map<String, Object> consumerConfigs()
    {
        Map<String, Object> props = new HashMap<>();
        props.put( ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers );
        props.put( ConsumerConfig.GROUP_ID_CONFIG, groupId );
        props.put( ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class );
        props.put( ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class );
        props.put( JacksonJsonDeserializer.TRUSTED_PACKAGES, "io.zaplink.notification.dto.request" );
        props.put( JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, "io.zaplink.notification.dto.request.EmailRequest" );
        props.put( ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest" );
        return props;
    }

    @Bean
    public ConsumerFactory<String, EmailRequest> consumerFactory()
    {
        return new DefaultKafkaConsumerFactory<>( consumerConfigs(),
                                                  new StringDeserializer(),
                                                  new JacksonJsonDeserializer<>( EmailRequest.class, false ) );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EmailRequest> kafkaListenerContainerFactory()
    {
        ConcurrentKafkaListenerContainerFactory<String, EmailRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory( consumerFactory() );
        return factory;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory()
    {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put( ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers );
        configProps.put( ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class );
        configProps.put( ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class );
        return new DefaultKafkaProducerFactory<>( configProps );
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate()
    {
        return new KafkaTemplate<>( producerFactory() );
    }
}