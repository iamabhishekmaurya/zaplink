package io.zaplink.apigateway.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Java Configuration for Cloud Gateway Routes (Dynamic/Complex Routes).
 * Part of the Hybrid Gateway Configuration.
 */
@Slf4j @Configuration
public class GatewayConfig
{
        @Bean
        public RouteLocator customRouteLocator( RouteLocatorBuilder builder )
        {
                log.info( "🚀 Initializing Hybrid Gateway Java Routes..." );
                return builder.routes()
                                /**
                                 * Manager Service Redirect Route
                                 * Moved to Java to show custom header and logging logic.
                                 */
                                .route( "manager",
                                        r -> r.path( "/r/**" ).filters( f -> f
                                                        .rewritePath( "/r/(?<segment>.*)", "/manager/${segment}" )
                                                        .addResponseHeader( "X-Zaplink-Processing-Time",
                                                                            LocalDateTime.now().toString() )
                                                        .addResponseHeader( "X-Zaplink-Mode", "HYBRID" ) )
                                                        .uri( "http://localhost:8083" ) )
                                /* 
                                   NOTE: 'shortner' and 'processor' routes are handled 
                                   via application.yml (Static Routes).
                                */
                                .build();
        }
}
