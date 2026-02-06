package io.zaplink.auth.common.config;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter that adds a default API version header for excluded paths.
 * This allows Swagger, actuator, and health endpoints to work without
 * requiring clients to provide the X-API-Version header.
 */
@Component @Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiVersionBypassFilter
    extends
    OncePerRequestFilter
{
    private static final String   API_VERSION_HEADER = "X-API-Version";
    private static final String   DEFAULT_VERSION    = "1";
    private static final String[] EXCLUDED_PATHS     =
    { "/v3/api-docs", "/swagger-ui", "/swagger-ui.html", "/actuator", "/health" };
    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain )
        throws ServletException,
        IOException
    {
        String path = request.getRequestURI();
        // Check if this is an excluded path and no version header is present
        if ( isExcludedPath( path ) && request.getHeader( API_VERSION_HEADER ) == null )
        {
            // Wrap the request to inject the default version header
            HttpServletRequest wrappedRequest = new ApiVersionRequestWrapper( request, DEFAULT_VERSION );
            filterChain.doFilter( wrappedRequest, response );
        }
        else
        {
            filterChain.doFilter( request, response );
        }
    }

    private boolean isExcludedPath( String path )
    {
        if ( path == null )
        {
            return false;
        }
        for ( String excluded : EXCLUDED_PATHS )
        {
            if ( path.startsWith( excluded ) )
            {
                return true;
            }
        }
        return false;
    }
    /**
     * Request wrapper that adds the API version header.
     */
    private static class ApiVersionRequestWrapper
        extends
        HttpServletRequestWrapper
    {
        private final String version;
        public ApiVersionRequestWrapper( HttpServletRequest request, String version )
        {
            super( request );
            this.version = version;
        }

        @Override
        public String getHeader( String name )
        {
            if ( API_VERSION_HEADER.equalsIgnoreCase( name ) )
            {
                return version;
            }
            return super.getHeader( name );
        }

        @Override
        public Enumeration<String> getHeaders( String name )
        {
            if ( API_VERSION_HEADER.equalsIgnoreCase( name ) )
            {
                return Collections.enumeration( List.of( version ) );
            }
            return super.getHeaders( name );
        }

        @Override
        public Enumeration<String> getHeaderNames()
        {
            List<String> headerNames = Collections.list( super.getHeaderNames() );
            if ( !headerNames.contains( API_VERSION_HEADER ) )
            {
                headerNames.add( API_VERSION_HEADER );
            }
            return Collections.enumeration( headerNames );
        }
    }
}
