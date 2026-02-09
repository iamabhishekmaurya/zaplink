package io.zaplink.manager.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;

/**
 * Feign client configuration for JSON encoding/decoding.
 */
@Configuration @RequiredArgsConstructor
public class FeignConfig
{
    private final ObjectMapper objectMapper;
    @Bean
    public Encoder feignEncoder()
    {
        return new JacksonEncoder( objectMapper );
    }

    @Bean
    public Decoder feignDecoder()
    {
        return new JacksonDecoder( objectMapper );
    }
}
