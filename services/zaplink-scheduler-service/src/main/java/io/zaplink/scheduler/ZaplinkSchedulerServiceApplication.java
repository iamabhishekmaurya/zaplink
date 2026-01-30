package io.zaplink.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication @EnableFeignClients
public class ZaplinkSchedulerServiceApplication
{
	public static void main( String[] args )
	{
		SpringApplication.run( ZaplinkSchedulerServiceApplication.class, args );
	}
}
