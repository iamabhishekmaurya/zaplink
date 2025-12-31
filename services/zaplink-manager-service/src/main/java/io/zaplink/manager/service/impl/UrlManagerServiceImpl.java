package io.zaplink.manager.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import io.zaplink.manager.common.constants.LogConstants;
import io.zaplink.manager.common.constants.RedisConstants;
import io.zaplink.manager.common.enums.UrlStatusEnum;
import io.zaplink.manager.common.trace.TraceContext;
import io.zaplink.manager.dto.request.AnalyticsEvent;
import io.zaplink.manager.dto.response.LinkResponse;
import io.zaplink.manager.dto.response.StatsResponse;
import io.zaplink.manager.entity.UrlMappingEntity;
import io.zaplink.manager.repository.UrlAnalyticsRepository;
import io.zaplink.manager.repository.UrlMappingRepository;
import io.zaplink.manager.service.UrlManagerService;
import io.zaplink.manager.service.helper.KafkaServiceHelper;
import io.zaplink.manager.service.helper.RedisServiceHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @Slf4j @RequiredArgsConstructor
public class UrlManagerServiceImpl
    implements
    UrlManagerService
{
    private final RedisServiceHelper     redisService;
    private final KafkaServiceHelper     kafkaService;
    private final UrlMappingRepository   urlMappingRepository;
    private final UrlAnalyticsRepository urlAnalyticsRepository;
    @Override
    public String getShortUrl( AnalyticsEvent analyticsEvent )
    {
        String key = analyticsEvent.getUrlKey();
        try
        {
            String originalUrl = redisService.getValue( key );
            if ( originalUrl != null && !originalUrl.isEmpty() )
            {
                log.info( LogConstants.LOG_URL_FOUND_IN_REDIS, key );
                // Async: Publish click event (fire-and-forget)
                publishAnalyticsEventAsync( analyticsEvent );
                return originalUrl;
            }
            else
            {
                log.info( LogConstants.LOG_URL_NOT_FOUND_IN_REDIS, key );
                Optional<UrlMappingEntity> optionalUrlMappingEntity = urlMappingRepository.findByShortUrlKey( key );
                if ( optionalUrlMappingEntity.isPresent() )
                {
                    UrlMappingEntity urlMappingEntity = optionalUrlMappingEntity.get();
                    if ( urlMappingEntity.getStatus().equals( UrlStatusEnum.ACTIVE ) )
                    {
                        log.info( LogConstants.LOG_URL_FOUND_IN_DB, key );
                        redisService.setValue( key, urlMappingEntity.getOriginalUrl(), RedisConstants.URL_CACHE_TTL );
                        // Async: Publish click event (fire-and-forget)
                        publishAnalyticsEventAsync( analyticsEvent );
                        return urlMappingEntity.getOriginalUrl();
                    }
                }
                else
                {
                    log.info( LogConstants.LOG_URL_NOT_FOUND_IN_DB, key );
                    return null;
                }
            }
        }
        catch ( Exception ex )
        {
            log.error( LogConstants.LOG_EXCEPTION_FETCHING_URL, key, ex.getMessage() );
        }
        return null;
    }

    @Override
    public List<LinkResponse> getLinksByUser( String userEmail )
    {
        return urlMappingRepository.findByUserEmail( userEmail ).stream().map( this::mapToLinkResponse )
                .collect( Collectors.toList() );
    }

    @Override
    public StatsResponse getUserStats( String userEmail )
    {
        long totalLinks = urlMappingRepository.countByUserEmail( userEmail );
        long activeLinks = urlMappingRepository.countByStatusAndUserEmail( UrlStatusEnum.ACTIVE, userEmail );
        Long totalClicks = urlMappingRepository.getTotalClickCountByUserEmail( userEmail );
        List<StatsResponse.Entry> clickTrend = urlAnalyticsRepository.findClickTrendByUser( userEmail ).stream()
                .map( obj -> StatsResponse.Entry.builder().name( obj[0].toString() ).value( obj[1] ).build() )
                .collect( Collectors.toList() );
        List<StatsResponse.Entry> referrers = urlAnalyticsRepository.findTopReferrersByUser( userEmail ).stream()
                .limit( 5 )
                .map( obj -> StatsResponse.Entry.builder().name( (String) obj[0] )
                        .value( calculatePercentage( (Long) obj[1], totalClicks ) ).build() )
                .collect( Collectors.toList() );
        List<Object[]> regions = urlAnalyticsRepository.findTopRegionsByUser( userEmail );
        String topRegion = regions.isEmpty() ? "N/A" : (String) regions.get( 0 )[0];
        // Format referrers to have int value as percentage
        return StatsResponse.builder().totalLinks( totalLinks ).activelinks( activeLinks )
                .totalClicks( totalClicks != null ? totalClicks : 0L ).clickTrend( clickTrend ).referrers( referrers )
                .topRegion( topRegion ).avgCtr( 0.0 ) // Impressions not tracked yet
                .build();
    }

    private int calculatePercentage( Long value, Long total )
    {
        if ( total == null || total == 0 )
            return 0;
        return (int) ( ( value * 100 ) / total );
    }

    @Override
    public void deleteLink( Long id, String userEmail )
    {
        urlMappingRepository.findById( id ).ifPresent( link -> {
            if ( link.getUserEmail().equals( userEmail ) )
            {
                urlMappingRepository.delete( link );
                redisService.deleteValue( link.getShortUrlKey() );
            }
        } );
    }

    private LinkResponse mapToLinkResponse( UrlMappingEntity entity )
    {
        return LinkResponse.builder().id( entity.getId() ).shortUrlKey( entity.getShortUrlKey() )
                .originalUrl( entity.getOriginalUrl() ).shortUrl( entity.getShortUrl() )
                .createdAt( entity.getCreatedAt() ).clickCount( entity.getClickCount() ).status( entity.getStatus() )
                .build();
    }

    /**
     * Publish analytics event (fire-and-forget)
     * @param analyticsEvent
     */
    @Async
    private void publishAnalyticsEventAsync( AnalyticsEvent analyticsEvent )
    {
        log.info( LogConstants.LOG_CLICK_EVENT_PUBLISHED, analyticsEvent.getUrlKey() );
        try
        {
            analyticsEvent.setTraceId( TraceContext.getTraceId() );
            kafkaService.sendMessage( analyticsEvent );
        }
        catch ( Exception ex )
        {
            log.error( LogConstants.LOG_EXCEPTION_PUBLISHING_CLICK_EVENT, analyticsEvent.getUrlKey(), ex.getMessage() );
        }
    }
}
