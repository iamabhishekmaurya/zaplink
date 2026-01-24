package io.zaplink.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import tools.jackson.databind.ObjectMapper;

@SpringBootApplication @EnableFeignClients
public class ZaplinkManagerServiceApplication
{
	public static void main( String[] args )
	{
		SpringApplication.run( ZaplinkManagerServiceApplication.class, args );
	}

	@Bean
	public ObjectMapper objectMapper()
	{
		return new ObjectMapper();
	}
}
