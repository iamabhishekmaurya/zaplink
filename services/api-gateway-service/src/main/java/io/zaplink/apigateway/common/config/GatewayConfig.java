package io.zaplink.apigateway.common.config;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;     
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * Java Configuration for Cloud Gateway Routes (Dynamic/Complex Routes).
 * Part of the Hybrid Gateway Configuration.
 */
@Slf4j @Configuration
public class GatewayConfig
{
    @Value("${api.base-path:/v1/api}")
    private String apiBasePath;
    
    @Bean
    public RouteLocator customRouteLocator( RouteLocatorBuilder builder )
    {
        // Temporarily hardcode to test
        String basePath = "/v1/api";
        log.info( "🚀 Initializing Hybrid Gateway Java Routes with base path: {} (resolved: {})", apiBasePath, basePath );
        return builder.routes()
                /**
                 * Core Service Routes
                 */
                .route( "core",
                        r -> r.path( basePath + "/short/**" )
                                .filters( f -> f.stripPrefix(0) ) // Don't strip anything, keep full path
                                .uri( "http://localhost:8081" ) )
                .route( "core-qr",
                        r -> r.path( basePath + "/qr/**" )
                                .filters( f -> f.stripPrefix(0) ) // Don't strip anything, keep full path
                                .uri( "http://localhost:8081" ) )
                .route( "core-dynamic-qr",
                        r -> r.path( basePath + "/dynamic-qr/**" )
                                .filters( f -> f.stripPrefix(0) ) // Don't strip anything, keep full path
                                .uri( "http://localhost:8081" ) )
                /**
                 * Processor Service Route
                 */
                .route( "processor",
                        r -> r.path( "/processor/**" )
                                .filters( f -> f.stripPrefix(1) )
                                .uri( "http://localhost:8082" ) )
                /**
                 * Manager Service Routes
                 */
                .route( "manager",
                        r -> r.path( basePath + "/manager/**" )
                                .filters( f -> f.stripPrefix(0) ) // Don't strip anything, keep full path
                                .uri( "http://localhost:8083" ) )
                .route( "manager-short",
                        r -> r.path( "/r/**" )
                                .filters( f -> f
                                        .rewritePath( "/r/(?<segment>.*)", "/r/${segment}" )
                                        .addResponseHeader( "X-Zaplink-Processing-Time",
                                                            LocalDateTime.now().toString() )
                                        .addResponseHeader( "X-Zaplink-Mode", "HYBRID" ) )
                                .uri( "http://localhost:8081" ) )
                /**
                 * Auth Service Routes
                 */
                .route( "auth-service",
                        r -> r.path( basePath + "/auth/**" )
                                .filters( f -> f.stripPrefix(0) ) // Don't strip anything, keep full path
                                .uri( "http://localhost:8084" ) )
                .route( "user-service",
                        r -> r.path( basePath + "/users/**" )
                                .filters( f -> f.stripPrefix(2) )
                                .uri( "http://localhost:8084" ) )
                .build();
    }
}
