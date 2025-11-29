package io.zaplink.apigateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    private static final Logger log = LoggerFactory.getLogger(GatewayConfig.class);

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        log.info("Initializing custom routes...");
        return builder.routes()
                .route("shortner", r -> {
                    log.info("Configuring shortner route");
                    return r.path("/v1/shortner/**")
                            .filters(f -> f.stripPrefix(1))
                            .uri("http://zaplink-shortner-service:8081");
                })
                .route("manager", r -> {
                    log.info("Configuring manager route");
                    return r.path("/r/**")
                            .filters(f -> f.stripPrefix(1))
                            .uri("http://zaplink-manager-service:8083");
                })
                .route("processor", r -> {
                    log.info("Configuring processor route");
                    return r.path("/v1/processor/**")
                            .filters(f -> f.stripPrefix(1))
                            .uri("http://zaplink-processor-service:8082");
                })
                .build();
    }
}
