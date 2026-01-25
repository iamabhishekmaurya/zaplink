package io.zaplink.redirect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Zaplink Redirect Service Application.
 * 
 * High-performance redirect service optimized for:
 * - Low-latency redirects with Redis caching
 * - Async analytics via Kafka
 * - Virtual threads for high concurrency (Java 21)
 * 
 * @author Zaplink Team
 */
@SpringBootApplication
public class ZaplinkRedirectServiceApplication
{
	public static void main( String[] args )
	{
		SpringApplication.run( ZaplinkRedirectServiceApplication.class, args );
	}
}
