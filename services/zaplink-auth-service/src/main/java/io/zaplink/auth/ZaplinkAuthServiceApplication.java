package io.zaplink.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cache.annotation.EnableCaching;

@EnableCaching @SpringBootApplication
public class ZaplinkAuthServiceApplication
{
	public static void main( String[] args )
	{
		SpringApplication.run( ZaplinkAuthServiceApplication.class, args );
	}
}
