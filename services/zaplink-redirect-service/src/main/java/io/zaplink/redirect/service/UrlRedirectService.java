package io.zaplink.redirect.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import io.zaplink.redirect.common.enums.UrlStatusEnum;
import io.zaplink.redirect.dto.RedirectConfigDto;
import io.zaplink.redirect.dto.RedirectConfigDto.RedirectRuleDto;
import io.zaplink.redirect.dto.event.UrlClickEvent;
import io.zaplink.redirect.entity.RedirectRuleEntity;
import io.zaplink.redirect.entity.UrlMappingEntity;
import io.zaplink.redirect.repository.RedirectRuleRepository;
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
    private final UrlMappingRepository   urlMappingRepository;
    private final RedirectRuleRepository redirectRuleRepository;
    private final RedisService           redisService;
    private final GeoIpService           geoIpService;
    private final RuleEngine             ruleEngine;
    private final KafkaEventPublisher    kafkaEventPublisher;
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
        // 1. Try Redis cache (New Config format)
        Optional<RedirectConfigDto> cachedConfig = redisService.getUrlConfig( urlKey );
        RedirectConfigDto config = null;
        if ( cachedConfig.isPresent() )
        {
            config = cachedConfig.get();
        }
        else
        {
            // 2. Cache miss - query database
            // Try legacy string cache first (migration path)? No, just hit DB.
            Optional<UrlMappingEntity> entityOpt = urlMappingRepository.findByShortUrlKey( urlKey );
            if ( entityOpt.isEmpty() )
            {
                log.warn( "URL not found for key: {}", urlKey );
                return new RedirectResult.NotFound();
            }
            UrlMappingEntity entity = entityOpt.get();
            // Check status
            if ( entity.getStatus() != UrlStatusEnum.ACTIVE )
            {
                return switch ( entity.getStatus() )
                {
                    case EXPIRED -> new RedirectResult.Expired();
                    case DELETED, DISABLED -> new RedirectResult.Inactive();
                    default -> new RedirectResult.Inactive();
                };
            }
            // Check expiration
            if ( entity.getExpiresAt() != null && entity.getExpiresAt().isBefore( LocalDateTime.now() ) )
            {
                log.info( "URL expired for key: {}", urlKey );
                return new RedirectResult.Expired();
            }
            // Fetch Rules
            var rules = redirectRuleRepository.findByUrlMappingIdOrderByPriorityDesc( entity.getId() ).stream()
                    .map( r -> RedirectRuleDto.builder().dimension( r.getDimension() ).value( r.getValue() )
                            .destinationUrl( r.getDestinationUrl() ).priority( r.getPriority() ).build() )
                    .toList();
            config = RedirectConfigDto.builder().originalUrl( entity.getOriginalUrl() ).rules( rules ).build();
            // Cache it
            redisService.cacheUrlConfig( urlKey, config );
        }
        // 3. Smart Resolution
        String finalDestination = config.originalUrl();
        // Build Context
        // Build Context
        // TODO: Validate Business Plan Plan (Mock for now - implementation requires fetching User Plan from DB/Cache)
        boolean hasBusinessPlan = true;
        if ( !config.rules().isEmpty() && hasBusinessPlan )
        {
            String ip = RequestUtils.getClientIpAddress( request );
            String ua = RequestUtils.getUserAgent( request );
            // Extract Country from Cloudflare Header or GeoIP
            String country = request.getHeader( "CF-IPCountry" );
            if ( country == null || country.isEmpty() )
            {
                country = geoIpService.resolveLocation( ip ).get( "country" );
            }
            var context = RuleEngine.RoutingContext.builder().deviceType( RequestUtils.extractDeviceType( ua ) )
                    .os( RequestUtils.extractOS( ua ) ).country( country ).build();
            // Map DTO rules to Entity rules for Engine (or overload Engine to take DTO)
            // I'll overload Engine to take DTO or map back. Mapping back is silly.
            // I should update RuleEngine to be generic or take DTO.
            // For now, I'll map DTO back to Entity or creates a simple adapter?
            // Actually RuleEngine takes List<RedirectRuleEntity>.
            // I will modify RuleEngine to take List<? extends Rule> interface?
            // Or i'll just create dummy entities from DTO.
            var entityRules = config
                    .rules().stream().map( r -> RedirectRuleEntity.builder().dimension( r.dimension() )
                            .value( r.value() ).destinationUrl( r.destinationUrl() ).priority( r.priority() ).build() )
                    .toList();
            Optional<String> smartDest = ruleEngine.evaluate( entityRules, context );
            if ( smartDest.isPresent() )
            {
                finalDestination = smartDest.get();
            }
        }
        publishClickEvent( urlKey, request );
        return new RedirectResult.Success( finalDestination );
    }

    /**
     * Publish click event asynchronously to Kafka.
     */
    @Async
    public void publishClickEvent( String urlKey, HttpServletRequest request )
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
