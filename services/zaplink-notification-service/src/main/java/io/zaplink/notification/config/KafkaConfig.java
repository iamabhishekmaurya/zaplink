// package io.zaplink.notification.config;

// import java.util.HashMap;
// import java.util.Map;

// import org.apache.kafka.clients.consumer.ConsumerConfig;
// import org.apache.kafka.common.serialization.StringDeserializer;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
// import org.springframework.kafka.core.ConsumerFactory;
// import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
// import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

// @Configuration
// public class KafkaConfig
// {
//     @Value("${spring.kafka.bootstrap-servers}")
//     private String bootstrapServers;
//     @Bean
//     public ConsumerFactory<String, Object> consumerFactory()
//     {
//         Map<String, Object> configProps = new HashMap<>();
//         configProps.put( ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers );
//         configProps.put( ConsumerConfig.GROUP_ID_CONFIG, "zaplink-consumer-group" );
//         configProps.put( ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class );
//         configProps.put( ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JacksonJsonDeserializer.class );
//         configProps.put( JacksonJsonDeserializer.TRUSTED_PACKAGES, "*" );
//         // Create a custom JsonDeserializer with the target type
//         JacksonJsonDeserializer<Object> jsonDeserializer = new JacksonJsonDeserializer<>();
//         jsonDeserializer.addTrustedPackages( "*" ); // Trust all packages for deserialization
//         return new DefaultKafkaConsumerFactory<>( configProps, new StringDeserializer(), jsonDeserializer );
//     }

//     @Bean
//     public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory()
//     {
//         ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
//         factory.setConsumerFactory( consumerFactory() );
//         return factory;
//     }
// }