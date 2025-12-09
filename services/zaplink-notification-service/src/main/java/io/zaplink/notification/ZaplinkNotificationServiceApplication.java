package io.zaplink.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class ZaplinkNotificationServiceApplication
{
	public static void main( String[] args )
	{
		SpringApplication.run( ZaplinkNotificationServiceApplication.class, args );
	}
}
