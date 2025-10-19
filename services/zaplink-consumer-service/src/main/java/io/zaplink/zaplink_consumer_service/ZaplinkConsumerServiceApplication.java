package io.zaplink.zaplink_consumer_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ZaplinkConsumerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZaplinkConsumerServiceApplication.class, args);
	}

}
