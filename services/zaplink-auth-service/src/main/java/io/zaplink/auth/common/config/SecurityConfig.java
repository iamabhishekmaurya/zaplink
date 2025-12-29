package io.zaplink.auth.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import io.zaplink.auth.common.constants.ApiConstants;
import io.zaplink.auth.common.constants.LogConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Simplified Spring Security configuration class for auth service.
 * Only handles authentication, JWT validation has been moved to API Gateway.
 * 
 * Features:
 * - Basic authentication for login endpoints
 * - Password encryption with BCrypt
 * - Method-level security enabled
 * - Public endpoints for authentication operations
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2025-11-30
 */
@Slf4j
@Configuration 
@EnableWebSecurity 
@RequiredArgsConstructor 
@EnableMethodSecurity(prePostEnabled = true) 
public class SecurityConfig
{
    /**
     * Configures password encoder for secure password hashing.
     * Uses BCrypt algorithm with default strength (10).
     * 
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        log.debug( LogConstants.LOG_CONFIGURING_BCRYPT_PASSWORD_ENCODER );
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures authentication manager for Spring Security.
     * Used for authenticating users during login.
     * 
     * @param config AuthenticationConfiguration from Spring Security
     * @return AuthenticationManager instance
     * @throws Exception if configuration fails
     */
    @Bean
    public AuthenticationManager authenticationManager( AuthenticationConfiguration config )
        throws Exception
    {
        log.debug( LogConstants.LOG_CONFIGURING_SPRING_SECURITY_AUTH_MANAGER );
        return config.getAuthenticationManager();
    }

    /**
     * Configures the main security filter chain.
     * Sets up basic security for auth endpoints only.
     * 
     * @param http HttpSecurity configuration object
     * @return Configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain( HttpSecurity http )
        throws Exception
    {
        log.debug( LogConstants.LOG_CONFIGURING_SECURITY_FILTER_CHAIN );
        http
                // Disable CSRF for stateless authentication
                .csrf( csrf -> {
                    log.debug( LogConstants.LOG_DISABLING_CSRF_PROTECTION );
                    csrf.disable();
                } )
                // Configure stateless session management
                .sessionManagement( session -> {
                    log.debug( LogConstants.LOG_CONFIGURING_STATELESS_SESSION_MANAGEMENT );
                    session.sessionCreationPolicy( SessionCreationPolicy.STATELESS );
                } )
                // Configure endpoint authorization rules
                .authorizeHttpRequests( auth -> {
                    log.debug( LogConstants.LOG_CONFIGURING_ENDPOINT_AUTHORIZATION_RULES );
                    // Public endpoints - no authentication required
                    auth.requestMatchers( ApiConstants.AUTH_BASE_PATH ).permitAll()
                            .requestMatchers( ApiConstants.AUTH_V1_BASE_PATH ).permitAll()
                            .requestMatchers( ApiConstants.ERROR_PATH ).permitAll()
                            .requestMatchers( ApiConstants.ACTUATER_PATH ).permitAll()
                            // All other endpoints require authentication
                            .anyRequest().authenticated();
                } );
        log.info( LogConstants.LOG_SPRING_SECURITY_FILTER_CHAIN_CONFIGURED );
        log.debug( LogConstants.LOG_PUBLIC_ENDPOINTS );
        log.debug( LogConstants.LOG_ALL_OTHER_ENDPOINTS_REQUIRE_AUTH );
        return http.build();
    }
}
