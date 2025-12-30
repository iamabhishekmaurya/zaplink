package io.zaplink.apigateway.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import io.zaplink.apigateway.common.constant.LogConstants;
import io.zaplink.apigateway.common.security.JwtAuthenticationFilter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j @Configuration @EnableWebFluxSecurity @RequiredArgsConstructor
public class SecurityConfig
{
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityWebFilterChain securityWebFilterChain( ServerHttpSecurity http )
    {
        log.debug( LogConstants.LOG_CONFIGURING_SECURITY_FILTER_CHAIN );
        return http.csrf( ServerHttpSecurity.CsrfSpec::disable )
                .cors( cors -> cors.configurationSource( corsConfigurationSource() ) )
                .authorizeExchange( exchanges -> exchanges
                        .pathMatchers( "/auth/register", "/auth/login", "/auth/refresh", "/auth/verify-email",
                                       "/auth/resend-verification", "/auth/request-password-reset",
                                       "/auth/reset-password" )
                        .permitAll().pathMatchers( "/r/**" ).permitAll().pathMatchers( "/error" ).permitAll()
                        .pathMatchers( "/actuator/**" ).permitAll().pathMatchers( HttpMethod.OPTIONS, "/**" )
                        .permitAll().anyExchange().authenticated() )
                .addFilterBefore( jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION ).build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns( Arrays.asList( "http://localhost:*", "http://127.0.0.1:*" ) );
        configuration.setAllowedMethods( Arrays.asList( "GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD" ) );
        configuration.setAllowedHeaders( Arrays.asList( "*" ) );
        configuration.setAllowCredentials( true );
        configuration.setMaxAge( 3600L );
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration( "/**", configuration );
        return source;
    }
}
