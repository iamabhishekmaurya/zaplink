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
                // Base path from config (default /api)
                String basePath = apiBasePath;
                String writePath = basePath + "/wr";
                String readPath = basePath + "/rd";
                log.info( "🚀 Initializing Hybrid Gateway Java Routes with write path: {} and read path: {}", writePath,
                          readPath );
                return builder.routes()
                                /**
                                 * Core Service Routes (Write Operations)
                                 * Port: 8081
                                 */
                                .route( "core-short-write", r -> r.path( writePath + "/short/url" ).and()
                                                .header( "X-API-Version", "1" )
                                                .filters( f -> f.rewritePath( "/api/wr/short/url", "/core/url" ) )
                                                .uri( "http://localhost:8081" ) )
                                .route( "core-qr-write",
                                        r -> r.path( writePath + "/qr/**" ).and().header( "X-API-Version", "1" )
                                                        .filters( f -> f.rewritePath( "/api/wr/qr/(?<segment>.*)",
                                                                                      "/core/qr/${segment}" ) )
                                                        .uri( "http://localhost:8081" ) )
                                .route( "manager-dynamic-qr-write",
                                        r -> r.path( writePath + "/dyqr/**" ).and().header( "X-API-Version", "1" )
                                                        .filters( f -> f.rewritePath( "/api/wr/(?<segment>.*)",
                                                                                      "/${segment}" ) )
                                                        .uri( "http://localhost:8083" ) )
                                /**
                                 * Processor Service Route
                                 * Port: 8082
                                 */
                                .route( "processor",
                                        r -> r.path( "/processor/**" ).filters( f -> f.stripPrefix( 1 ) )
                                                        .uri( "http://localhost:8082" ) )
                                /**
                                 * Manager Service Routes (Read Operations)
                                 * Port: 8083
                                 */
                                .route( "manager-short-read",
                                        r -> r.path( readPath + "/short/**" ).and().header( "X-API-Version", "1" )
                                                        .filters( f -> f.rewritePath( "/api/rd/(?<segment>.*)",
                                                                                      "/${segment}" ) )
                                                        .uri( "http://localhost:8083" ) )
                                .route( "manager-dynamic-qr-read",
                                        r -> r.path( readPath + "/dyqr/**" ).and().header( "X-API-Version", "1" )
                                                        .filters( f -> f.rewritePath( "/api/rd/(?<segment>.*)",
                                                                                      "/${segment}" ) )
                                                        .uri( "http://localhost:8083" ) )
                                /**
                                 * Redirect Service Routes (High-performance redirects)
                                 * Port: 8085
                                 */
                                .route( "redirect-link", r -> r.path( "/r/**" )
                                                .filters( f -> f.rewritePath( "/r/(?<segment>.*)", "/r/${segment}" )
                                                                .addResponseHeader( "X-Zaplink-Processing-Time",
                                                                                    LocalDateTime.now().toString() )
                                                                .addResponseHeader( "X-Zaplink-Mode", "HYBRID" ) )
                                                .uri( "http://localhost:8085" ) )
                                .route( "redirect-qr", r -> r.path( "/s/**" )
                                                .filters( f -> f.rewritePath( "/s/(?<segment>.*)", "/s/${segment}" )
                                                                .addResponseHeader( "X-Zaplink-Processing-Time",
                                                                                    LocalDateTime.now().toString() )
                                                                .addResponseHeader( "X-Zaplink-Mode", "HYBRID" ) )
                                                .uri( "http://localhost:8085" ) )
                                /**
                                 * Auth Service Routes
                                 * Port: 8084
                                 */
                                .route( "auth-service",
                                        r -> r.path( basePath + "/auth/**" ).and().header( "X-API-Version", "1" )
                                                        .filters( f -> f.stripPrefix( 1 ) )
                                                        .uri( "http://localhost:8084" ) )
                                .route( "user-service",
                                        r -> r.path( basePath + "/users/**" ).and().header( "X-API-Version", "1" )
                                                        .filters( f -> f.stripPrefix( 1 ) ) // Changed from 0 to 1 assuming /api/users -> /users
                                                        .uri( "http://localhost:8084" ) )
                                .build();
        }
}
