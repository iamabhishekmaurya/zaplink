package io.zaplink.apigateway.common.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import io.zaplink.apigateway.common.config.JwtConfig;
import io.zaplink.apigateway.common.constant.LogConstants;
import io.zaplink.apigateway.common.constant.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j @Component @RequiredArgsConstructor
public class JwtAuthenticationFilter
    implements
    WebFilter
{
    private final JwtConfig jwtConfig;
    @Override
    public Mono<Void> filter( ServerWebExchange exchange, WebFilterChain chain )
    {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();
        // Skip authentication for OPTIONS requests (CORS preflight) and public endpoints
        if ( request.getMethod().name().equals( "OPTIONS" ) || isPublicEndpoint( path ) )
        {
            return chain.filter( exchange );
        }
        log.debug( LogConstants.LOG_GATEWAY_FILTER_PROCESSING_REQUEST, request.getMethod(), path );
        final String authHeader = request.getHeaders().getFirst( SecurityConstants.AUTH_HEADER );
        if ( authHeader == null || !authHeader.startsWith( SecurityConstants.BEARER_PREFIX ) )
        {
            log.debug( LogConstants.LOG_GATEWAY_FILTER_AUTH_HEADER_MISSING );
            return handleUnauthorized( exchange );
        }
        try
        {
            String jwt = authHeader.substring( SecurityConstants.BEARER_PREFIX_LENGTH );
            String userEmail = jwtConfig.extractUsername( jwt );
            if ( userEmail != null )
            {
                // Create a simple UserDetails object for authentication
                UserDetails userDetails = User.builder().username( userEmail ).password( "" ) // Password not needed for JWT validation
                        .authorities( "ROLE_USER" ).build();
                if ( jwtConfig.validateToken( jwt, userDetails.getUsername() ) )
                {
                    log.debug( LogConstants.LOG_GATEWAY_FILTER_TOKEN_VALIDATION_PASSED, userEmail );
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken( userDetails,
                                                                                                             null,
                                                                                                             userDetails
                                                                                                                     .getAuthorities() );
                    SecurityContext securityContext = new SecurityContextImpl( authToken );
                    log.debug( LogConstants.LOG_GATEWAY_FILTER_SETTING_AUTHENTICATION, userEmail );
                    // Add user email to request header for downstream services
                    ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                            .header( "X-User-Email", userEmail ).build();
                    return chain.filter( exchange.mutate().request( mutatedRequest ).build() )
                            .contextWrite( ReactiveSecurityContextHolder
                                    .withSecurityContext( Mono.just( securityContext ) ) );
                }
                else
                {
                    log.debug( LogConstants.LOG_GATEWAY_FILTER_TOKEN_VALIDATION_FAILED, "Token validation failed" );
                    return handleUnauthorized( exchange );
                }
            }
        }
        catch ( Exception ex )
        {
            log.debug( LogConstants.LOG_GATEWAY_FILTER_TOKEN_VALIDATION_FAILED, ex.getMessage() );
            return handleUnauthorized( exchange );
        }
        return chain.filter( exchange );
    }

    private boolean isPublicEndpoint( String path )
    {
        // Whitelist only specific public auth endpoints
        boolean isAuthPublic = ( path.equals( "/auth/register" ) || path.equals( "/auth/login" )
                || path.equals( "/auth/refresh" ) || path.equals( "/auth/verify-email" )
                || path.equals( "/auth/resend-verification" ) || path.equals( "/auth/request-password-reset" )
                || path.equals( "/auth/reset-password" ) );
        return isAuthPublic || path.equals( "/error" ) || path.startsWith( "/actuator" ) || path.startsWith( "/r/" );
    }

    private Mono<Void> handleUnauthorized( ServerWebExchange exchange )
    {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode( HttpStatus.UNAUTHORIZED );
        response.getHeaders().add( HttpHeaders.CONTENT_TYPE, "application/json" );
        String errorBody = "{\"error\":\"Unauthorized\",\"message\":\"Invalid or missing JWT token\"}";
        return response.writeWith( Mono.just( response.bufferFactory().wrap( errorBody.getBytes() ) ) );
    }
}
