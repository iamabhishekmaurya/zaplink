package io.zaplink.apigateway.common.trace;

import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import io.zaplink.apigateway.common.constant.AppConstants;
import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * WebFlux filter that extracts trace ID from headers or generates a new one,
 * and propagates it through both Reactor context and MDC for logging.
 * Ensures trace ID is available throughout the request lifecycle.
 */
@Component @Order(1)
public class TraceIdFilter
    implements
    WebFilter
{
    private static final String TRACE_ID = AppConstants.TRACE_ID_MDC_KEY;
    @PostConstruct
    public void init()
    {
        // Enable automatic context propagation for all schedulers
        Hooks.enableAutomaticContextPropagation();
        // Add a hook to propagate MDC from Reactor context
        Schedulers.onScheduleHook( "mdc", runnable -> {
            String traceId = MDC.get( TRACE_ID );
            return () -> {
                try
                {
                    if ( traceId != null )
                    {
                        MDC.put( TRACE_ID, traceId );
                    }
                    runnable.run();
                }
                finally
                {
                    MDC.remove( TRACE_ID );
                }
            };
        } );
    }

    @Override
    public Mono<Void> filter( ServerWebExchange exchange, WebFilterChain chain )
    {
        // Get traceId from header or generate new one
        String traceId = exchange.getRequest().getHeaders().getFirst( AppConstants.TRACE_ID_HEADER );
        if ( traceId == null || traceId.isEmpty() )
        {
            traceId = UUID.randomUUID().toString();
        }
        // Add traceId to response header for downstream services
        exchange.getResponse().getHeaders().add( AppConstants.TRACE_ID_HEADER, traceId );
        // Put traceId in MDC immediately for the current thread
        MDC.put( TRACE_ID, traceId );
        String finalTraceId = traceId;
        /**
         * Propagate traceId using Reactor Context:
         * contextWrite() adds traceId to Reactor context for downstream operators
         * The scheduler hook will handle MDC propagation to background threads
         */
        return chain.filter( exchange ).contextWrite( ctx -> ctx.put( TRACE_ID, finalTraceId ) )
                .doFinally( signal -> MDC.clear() );
    }
}
