package io.zaplink.apigateway.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import io.zaplink.apigateway.common.constant.LogConstants;
import io.zaplink.apigateway.common.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig
{
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain( ServerHttpSecurity http )
    {
        log.debug( LogConstants.LOG_CONFIGURING_SECURITY_FILTER_CHAIN );
        
        return http
            .csrf( ServerHttpSecurity.CsrfSpec::disable )
            .authorizeExchange( exchanges -> exchanges
                .pathMatchers( "/auth/**", "/v1/auth/**" ).permitAll()
                .pathMatchers( "/error" ).permitAll()
                .pathMatchers( "/actuator/**" ).permitAll()
                .pathMatchers( HttpMethod.OPTIONS, "/**" ).permitAll()
                .anyExchange().authenticated()
            )
            .addFilterBefore( jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION )
            .build();
    }
}
