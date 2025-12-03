package io.zaplink.auth.common.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.zaplink.auth.common.config.JwtConfig;
import io.zaplink.auth.common.constants.SecurityConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component @RequiredArgsConstructor @Slf4j
public class JwtAuthenticationFilter
    extends
    OncePerRequestFilter
{
    private final JwtConfig          jwtConfig;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain )
        throws ServletException,
        IOException
    {
        final String authHeader = request.getHeader( SecurityConstants.AUTH_HEADER );
        final String jwt;
        final String userEmail;
        if ( authHeader == null || !authHeader.startsWith( SecurityConstants.BEARER_PREFIX ) )
        {
            filterChain.doFilter( request, response );
            return;
        }
        jwt = authHeader.substring( SecurityConstants.BEARER_PREFIX_LENGTH );
        userEmail = jwtConfig.extractUsername( jwt );
        if ( userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null )
        {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername( userEmail );
            if ( jwtConfig.validateToken( jwt, userDetails.getUsername() ) )
            {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken( userDetails,
                                                                                                         null,
                                                                                                         userDetails
                                                                                                                 .getAuthorities() );
                authToken.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
                SecurityContextHolder.getContext().setAuthentication( authToken );
            }
        }
        filterChain.doFilter( request, response );
    }
}
