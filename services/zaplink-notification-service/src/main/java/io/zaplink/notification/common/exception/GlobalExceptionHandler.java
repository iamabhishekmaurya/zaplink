package io.zaplink.notification.common.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import io.zaplink.notification.common.constants.AppConstants;
import io.zaplink.notification.common.constants.LogConstants;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Global exception handler for WebFlux applications that automatically includes trace IDs
 * in all error logging. This handler makes trace ID logging transparent to business logic.
 */
@Slf4j @RestControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(Exception.class)
    public Mono<Map<String, Object>> handleGlobalException( Exception ex, ServerWebExchange exchange )
    {
        String traceId = exchange.getRequest().getHeaders().getFirst( AppConstants.TRACE_ID_HEADER );
        if ( traceId == null )
        {
            traceId = AppConstants.TRACE_ID_NOT_AVAILABLE;
        }
        log.error( LogConstants.LOG_UNEXPECTED_EXCEPTION_OCCURRED, traceId, ex.getMessage(), ex );
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put( AppConstants.JSON_KEY_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value() );
        errorResponse.put( AppConstants.JSON_KEY_ERROR, AppConstants.HTTP_INTERNAL_SERVER_ERROR );
        errorResponse.put( AppConstants.JSON_KEY_MESSAGE, AppConstants.HTTP_UNEXPECTED_ERROR_MESSAGE );
        errorResponse.put( AppConstants.JSON_KEY_TRACE_ID, traceId );
        return Mono.just( errorResponse );
    }

    @ExceptionHandler(RuntimeException.class)
    public Mono<Map<String, Object>> handleRuntimeException( RuntimeException ex, ServerWebExchange exchange )
    {
        String traceId = exchange.getRequest().getHeaders().getFirst( AppConstants.TRACE_ID_HEADER );
        if ( traceId == null )
        {
            traceId = AppConstants.TRACE_ID_NOT_AVAILABLE;
        }
        log.error( LogConstants.LOG_RUNTIME_EXCEPTION_OCCURRED, traceId, ex.getMessage(), ex );
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put( AppConstants.JSON_KEY_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value() );
        errorResponse.put( AppConstants.JSON_KEY_ERROR, AppConstants.HTTP_RUNTIME_ERROR );
        errorResponse.put( AppConstants.JSON_KEY_MESSAGE, ex.getMessage() );
        errorResponse.put( AppConstants.JSON_KEY_TRACE_ID, traceId );
        return Mono.just( errorResponse );
    }
}
