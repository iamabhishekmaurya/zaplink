package io.zaplink.core.common.trace;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

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
        String traceId = TraceContext.getTraceId();
        return traceId != null ? traceId : "N/A";
    }
}
