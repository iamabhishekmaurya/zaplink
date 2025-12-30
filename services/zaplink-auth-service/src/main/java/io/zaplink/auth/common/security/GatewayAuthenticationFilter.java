package io.zaplink.auth.common.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Filter that extracts user identity from Gateway headers.
 * Trusts the X-User-Email header passed by the API Gateway.
 */
@Slf4j @Component @RequiredArgsConstructor
public class GatewayAuthenticationFilter
    extends
    OncePerRequestFilter
{
    private final CustomUserDetailsService userDetailsService;
    private static final String            X_USER_EMAIL = "X-User-Email";
    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain )
        throws ServletException,
        IOException
    {
        String userEmail = request.getHeader( X_USER_EMAIL );
        if ( userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null )
        {
            log.debug( "Processing authentication for user: {}", userEmail );
            try
            {
                UserDetails userDetails = userDetailsService.loadUserByUsername( userEmail );
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken( userDetails,
                                                                                                         null,
                                                                                                         userDetails
                                                                                                                 .getAuthorities() );
                authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
                SecurityContextHolder.getContext().setAuthentication( authToken );
                log.debug( "Authentication successful for user: {}", userEmail );
            }
            catch ( Exception e )
            {
                log.error( "Authentication failed for user: {}", userEmail, e );
            }
        }
        filterChain.doFilter( request, response );
    }
}
