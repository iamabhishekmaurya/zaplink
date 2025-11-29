package io.zaplink.shortner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ZaplinkShortnerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZaplinkShortnerApplication.class, args);
	}

}
