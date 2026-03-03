package io.zaplink.apigateway.common.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import io.zaplink.apigateway.common.constant.LogConstants;
import io.zaplink.apigateway.common.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j @Configuration @EnableWebFluxSecurity @RequiredArgsConstructor
public class SecurityConfig
{
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public ReactiveAuthenticationManager reactiveAuthenticationManager()
    {
        return authentication -> Mono.empty();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain( ServerHttpSecurity http )
    {
        log.debug( LogConstants.LOG_CONFIGURING_SECURITY_FILTER_CHAIN );
        return http.csrf( ServerHttpSecurity.CsrfSpec::disable )
                .cors( cors -> cors.configurationSource( corsConfigurationSource() ) )
                .httpBasic( ServerHttpSecurity.HttpBasicSpec::disable )
                .formLogin( ServerHttpSecurity.FormLoginSpec::disable )
                .authorizeExchange( exchanges -> exchanges
                        .pathMatchers( "/api/auth/register", "/api/auth/login", "/api/auth/refresh",
                                       "/api/auth/verify-email", "/api/auth/resend-verification",
                                       "/api/auth/request-password-reset", "/api/auth/reset-password" )
                        .permitAll().pathMatchers( "/api/scraper/**" ).permitAll()
                        .pathMatchers( "/r/**", "/s/**", "/b/**" ).permitAll().pathMatchers( "/error" ).permitAll()
                        .pathMatchers( "/favicon.ico" ).permitAll().pathMatchers( "/.well-known/**" ).permitAll()
                        .pathMatchers( "/actuator/**" ).permitAll().pathMatchers( HttpMethod.OPTIONS, "/**" )
                        .permitAll().anyExchange().authenticated() )
                .addFilterBefore( jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION ).build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns( Arrays.asList( "http://localhost:3000", "http://127.0.0.1:3000" ) );
        configuration.setAllowedMethods( Arrays.asList( "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD" ) );
        configuration.setAllowedHeaders( Arrays.asList( "*" ) );
        configuration.setAllowCredentials( true );
        configuration.setMaxAge( 3600L );
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration( "/**", configuration );
        return source;
    }
}
