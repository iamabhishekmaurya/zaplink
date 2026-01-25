package io.zaplink.redirect.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.zaplink.redirect.dto.event.UrlClickEvent;
import io.zaplink.redirect.entity.UrlMappingEntity;
import io.zaplink.redirect.repository.UrlMappingRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for handling URL redirects.
 * Optimized for low-latency with Redis caching and async analytics.
 */
@Service @Slf4j @RequiredArgsConstructor
public class UrlRedirectService
{
    private final UrlMappingRepository urlMappingRepository;
    private final RedisService         redisService;
    private final GeoIpService         geoIpService;
    private final KafkaEventPublisher  kafkaEventPublisher;
    /**
     * Result of URL redirect resolution.
     */
    public sealed interface RedirectResult
        permits
        RedirectResult.Success,
        RedirectResult.NotFound,
        RedirectResult.Expired,
        RedirectResult.Inactive
    {
        record Success( String destinationUrl )
            implements
            RedirectResult
        {
        }
        record NotFound()
            implements
            RedirectResult
        {
        }
        record Expired()
            implements
            RedirectResult
        {
        }
        record Inactive()
            implements
            RedirectResult
        {
        }
    }
    /**
     * Resolve URL redirect and publish analytics event.
     *
     * @param urlKey  the short URL key
     * @param request the HTTP request for analytics
     * @return RedirectResult indicating success or failure reason
     */
    public RedirectResult resolveAndTrack( String urlKey, HttpServletRequest request )
    {
        log.debug( "Resolving URL redirect for key: {}", urlKey );
        // 1. Try Redis cache first
        Optional<String> cachedUrl = redisService.getUrl( urlKey );
        if ( cachedUrl.isPresent() )
        {
            publishClickEvent( urlKey, request );
            return new RedirectResult.Success( cachedUrl.get() );
        }
        // 2. Cache miss - query database
        Optional<UrlMappingEntity> entityOpt = urlMappingRepository.findByShortUrlKey( urlKey );
        if ( entityOpt.isEmpty() )
        {
            log.warn( "URL not found for key: {}", urlKey );
            return new RedirectResult.NotFound();
        }
        UrlMappingEntity entity = entityOpt.get();
        // 3. Check status using pattern matching
        return switch ( entity.getStatus() )
        {
            case ACTIVE -> {
                // Check expiration
                if ( entity.getExpiresAt() != null && entity.getExpiresAt().isBefore( LocalDateTime.now() ) )
                {
                    log.info( "URL expired for key: {}", urlKey );
                    yield new RedirectResult.Expired();
                }
                // Cache for future requests
                redisService.cacheUrl( urlKey, entity.getOriginalUrl() );
                // Publish analytics event
                publishClickEvent( urlKey, request );
                yield new RedirectResult.Success( entity.getOriginalUrl() );
            }
            case EXPIRED -> new RedirectResult.Expired();
            case DELETED, DISABLED -> new RedirectResult.Inactive();
        };
    }

    /**
     * Publish click event asynchronously to Kafka.
     */
    private void publishClickEvent( String urlKey, HttpServletRequest request )
    {
        try
        {
            String ipAddress = RequestUtils.getClientIpAddress( request );
            String userAgent = RequestUtils.getUserAgent( request );
            String referrer = RequestUtils.getReferrer( request );
            // Resolve location
            Map<String, String> location = geoIpService.resolveLocation( ipAddress );
            UrlClickEvent event = UrlClickEvent.of( urlKey, ipAddress, userAgent, referrer, location.get( "country" ),
                                                    location.get( "city" ), RequestUtils.extractDeviceType( userAgent ),
                                                    RequestUtils.extractBrowser( userAgent ),
                                                    UUID.randomUUID().toString() );
            kafkaEventPublisher.publishUrlClickEvent( event );
        }
        catch ( Exception e )
        {
            log.error( "Error publishing click event for key: {}", urlKey, e );
            // Don't fail redirect on analytics error
        }
    }
}
