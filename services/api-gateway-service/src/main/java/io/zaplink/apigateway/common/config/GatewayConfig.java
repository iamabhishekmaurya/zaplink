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
        @Value("${api.base-path:/api}")
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
                                .route( "core-short-write", r -> r.path( writePath + "/short/**" ).and()
                                                .header( "X-API-Version", "1" )
                                                .filters( f -> f.rewritePath( writePath + "/short/(?<segment>.*)",
                                                                              "/short/${segment}" ) )
                                                .uri( "http://localhost:8081" ) )
                                .route( "core-qr-write",
                                        r -> r.path( writePath + "/qr/**" ).and().header( "X-API-Version", "1" )
                                                        .filters( f -> f.rewritePath( writePath + "/qr/(?<segment>.*)",
                                                                                      "/qr/${segment}" ) )
                                                        .uri( "http://localhost:8081" ) )
                                /**
                                 * Core Service Bio Write Routes (CQRS Command Side)
                                 * Port: 8081
                                 */
                                .route( "core-bio-write",
                                        r -> r.path( writePath + "/bio/**" ).and().header( "X-API-Version", "1" )
                                                        .filters( f -> f.rewritePath( writePath + "/bio/(?<segment>.*)",
                                                                                      "/bio/${segment}" ) )
                                                        .uri( "http://localhost:8081" ) )
                                /**
                                 * Core Service Team Management Routes (Write Operations)
                                 * Port: 8081
                                 */
                                .route( "core-team-write", r -> r.path( writePath + "/teams/**" ).and()
                                                .header( "X-API-Version", "1" )
                                                .filters( f -> f.rewritePath( writePath + "/teams/(?<segment>.*)",
                                                                              "/teams/${segment}" ) )
                                                .uri( "http://localhost:8081" ) )
                                /**
                                 * Core Service Workflow Routes (Write Operations)
                                 * Port: 8081
                                 */
                                .route( "core-workflow-write", r -> r.path( writePath + "/workflow/**" ).and()
                                                .header( "X-API-Version", "1" )
                                                .filters( f -> f.rewritePath( writePath + "/workflow/(?<segment>.*)",
                                                                              "/workflow/${segment}" ) )
                                                .uri( "http://localhost:8081" ) )
                                /**
                                 * Manager Service Team Query Routes (Read Operations)
                                 * Port: 8083
                                 */
                                .route( "manager-team-read", r -> r.path( readPath + "/teams/**" ).and()
                                                .header( "X-API-Version", "1" )
                                                .filters( f -> f.rewritePath( readPath + "/teams/(?<segment>.*)",
                                                                              "/teams/${segment}" ) )
                                                .uri( "http://localhost:8083" ) )
                                /**
                                 * Manager Service Short URL Routes (Read Operations)
                                 * Port: 8083
                                 */
                                .route( "manager-short-read", r -> r.path( readPath + "/short/**" ).and()
                                                .header( "X-API-Version", "1" )
                                                .filters( f -> f.rewritePath( readPath + "/short/(?<segment>.*)",
                                                                              "/short/${segment}" ) )
                                                .uri( "http://localhost:8083" ) )
                                /**
                                 * Manager Service QR Routes (Read Operations)
                                 * Port: 8083
                                 */
                                .route( "manager-qr-read-exact",
                                        r -> r.path( readPath + "/qr" ).and().header( "X-API-Version", "1" )
                                                        .filters( f -> f.rewritePath( readPath + "/qr", "/qr" ) )
                                                        .uri( "http://localhost:8083" ) )
                                .route( "manager-qr-read",
                                        r -> r.path( readPath + "/qr/**" ).and().header( "X-API-Version", "1" )
                                                        .filters( f -> f.rewritePath( readPath + "/qr/(?<segment>.*)",
                                                                                      "/qr/${segment}" ) )
                                                        .uri( "http://localhost:8083" ) )
                                /**
                                 * Manager Service Bio Read Routes (CQRS Query Side)
                                 * Port: 8083
                                 */
                                .route( "manager-bio-read",
                                        r -> r.path( readPath + "/bio/**" ).and().header( "X-API-Version", "1" )
                                                        .filters( f -> f.rewritePath( readPath + "/bio/(?<segment>.*)",
                                                                                      "/bio/${segment}" ) )
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
                                 * Redirect Service Bio Page Public Route
                                 * Port: 8085
                                 */
                                .route( "redirect-bio-page", r -> r.path( "/b/**" )
                                                .filters( f -> f.rewritePath( "/b/(?<username>.*)", "/b/${username}" )
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
                                                        .filters( f -> f.stripPrefix( 1 ) )
                                                        .uri( "http://localhost:8084" ) )
                                /**
                                 * Media Service Routes
                                 * Port: 8086
                                 */
                                .route( "media-service", r -> r.path( basePath + "/media/**" ).and()
                                                .header( "X-API-Version", "1" )
                                                .filters( f -> f.rewritePath( basePath + "/media/(?<segment>.*)",
                                                                              "/media/${segment}" ) )
                                                .uri( "http://localhost:8086" ) )
                                .route( "media-folders", r -> r.path( basePath + "/folders/**" ).and()
                                                .header( "X-API-Version", "1" )
                                                .filters( f -> f.rewritePath( basePath + "/folders/(?<segment>.*)",
                                                                              "/folders/${segment}" ) )
                                                .uri( "http://localhost:8086" ) )
                                /**
                                 * Social Service Routes
                                 * Port: 8088
                                 */
                                .route( "social-service", r -> r.path( basePath + "/social/**" ).and()
                                                .header( "X-API-Version", "1" )
                                                .filters( f -> f.rewritePath( basePath + "/social/(?<segment>.*)",
                                                                              "/social/${segment}" ) )
                                                .uri( "http://localhost:8088" ) )
                                /**
                                 * Scheduler Service Routes
                                 * Port: 8089
                                 */
                                .route( "scheduler-service",
                                        r -> r.path( basePath + "/schedule/**" ).and().header( "X-API-Version", "1" )
                                                        .filters( f -> f.stripPrefix( 1 ) )
                                                        .uri( "http://localhost:8089" ) )
                                .build();
        }
}
