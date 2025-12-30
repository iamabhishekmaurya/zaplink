package io.zaplink.apigateway.common.trace;

import org.slf4j.MDC;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import io.zaplink.apigateway.common.constant.AppConstants;

/**
 * Logback converter that adds trace ID to log messages.
 * Usage in logback-spring.xml: %traceId
 * 
 * Example pattern: %d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level [%traceId] %logger{36} - %msg%n
 */
public class TraceIdConverter
    extends
    ClassicConverter
{
    @Override
    public String convert( ILoggingEvent event )
    {
        String traceId = MDC.get( AppConstants.TRACE_ID_MDC_KEY );
        return traceId != null ? traceId : AppConstants.TRACE_ID_DEFAULT_VALUE;
    }
}
