package io.zaplink.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication @EnableScheduling
public class ZaplinkProcessorApplication
{
	public static void main( String[] args )
	{
		SpringApplication.run( ZaplinkProcessorApplication.class, args );
	}
}