package io.zaplink.auth.common.trace;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import io.zaplink.auth.common.constants.LogConstants;

/**
 * Filter that extracts trace ID from request headers or generates a new one.
 * Ensures trace ID is available throughout the request lifecycle and
 * adds the trace ID to response headers for client-side tracking.
 */
@Slf4j 
@Component 
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter
    extends
    OncePerRequestFilter
{
    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain )
        throws ServletException,
        IOException
    {
        String traceId = extractOrGenerateTraceId( request );
        TraceContext.setTraceId( traceId );
        // Add trace ID to response header for client tracking
        response.setHeader( TraceContext.getTraceIdHeader(), traceId );
        log.info( LogConstants.LOG_REQUEST_STARTED_WITH_TRACE, request.getMethod(), request.getRequestURI(), traceId );
        try
        {
            filterChain.doFilter( request, response );
        }
        finally
        {
            log.info( LogConstants.LOG_REQUEST_COMPLETED_WITH_TRACE, request.getMethod(), request.getRequestURI(),
                      traceId, response.getStatus() );
            // TraceContext.clearTraceId();
        }
    }

    /**
     * Extracts trace ID from request header or generates a new one if not present.
     * 
     * @param request The HTTP request
     * @return The extracted or generated trace ID
     */
    private String extractOrGenerateTraceId( HttpServletRequest request )
    {
        String traceId = request.getHeader( TraceContext.getTraceIdHeader() );
        if ( traceId != null && !traceId.trim().isEmpty() )
        {
            log.debug( LogConstants.LOG_TRACE_ID_FOUND_IN_HEADER, traceId );
            return traceId.trim();
        }
        else
        {
            traceId = TraceContext.generateTraceId();
            log.debug( LogConstants.LOG_TRACE_ID_SET_FOR_THREAD, traceId );
            return traceId;
        }
    }

    @Override
    protected boolean shouldNotFilter( HttpServletRequest request )
    {
        String path = request.getRequestURI();
        // Skip trace ID processing for health checks and actuator endpoints
        return path.startsWith( "/actuator" ) || path.startsWith( "/health" );
    }
}
