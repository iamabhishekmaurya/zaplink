package io.zaplink.scheduler.common.config;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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
 * Filter to bypass API version check for Swagger UI and Actuator endpoints.
 * Injects the default API version header if missing for these specific paths.
 */
@Component @Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiVersionBypassFilter
    extends
    OncePerRequestFilter
{
    @Value("${spring.mvc.apiversion.use.header:X-API-Version}")
    private String                    versionHeader;
    @Value("${spring.mvc.apiversion.supported[0]:1}")
    private String                    defaultVersion;
    // Paths that should not require API versioning
    private static final List<String> BYPASS_PATHS = List.of( "/v3/api-docs", "/swagger-ui", "/actuator",
                                                              "/swagger-resources", "/webjars", "/error" );
    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain )
        throws ServletException,
        IOException
    {
        String requestURI = request.getRequestURI();
        // Check if the request path matches any of the bypass paths
        boolean shouldBypass = BYPASS_PATHS.stream().anyMatch( requestURI::startsWith );
        if ( shouldBypass && request.getHeader( versionHeader ) == null )
        {
            // Wrap the request to inject the default API version header
            HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper( request )
            {
                @Override
                public String getHeader( String name )
                {
                    if ( versionHeader.equalsIgnoreCase( name ) )
                    {
                        return defaultVersion;
                    }
                    return super.getHeader( name );
                }

                @Override
                public Enumeration<String> getHeaders( String name )
                {
                    if ( versionHeader.equalsIgnoreCase( name ) )
                    {
                        return Collections.enumeration( Collections.singletonList( defaultVersion ) );
                    }
                    return super.getHeaders( name );
                }

                @Override
                public Enumeration<String> getHeaderNames()
                {
                    List<String> names = Collections.list( super.getHeaderNames() );
                    if ( !names.contains( versionHeader ) )
                    {
                        names.add( versionHeader );
                    }
                    return Collections.enumeration( names );
                }
            };
            filterChain.doFilter( wrappedRequest, response );
        }
        else
        {
            filterChain.doFilter( request, response );
        }
    }
}
