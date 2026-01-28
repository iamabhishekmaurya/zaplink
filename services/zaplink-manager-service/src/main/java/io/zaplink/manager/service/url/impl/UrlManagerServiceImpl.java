package io.zaplink.manager.service.url.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.zaplink.manager.common.enums.UrlStatusEnum;
import io.zaplink.manager.dto.RedirectRuleDto;
import io.zaplink.manager.dto.response.LinkAnalyticsResponse;
import io.zaplink.manager.dto.response.LinkResponse;
import io.zaplink.manager.dto.response.StatsResponse;
import io.zaplink.manager.entity.RedirectRuleEntity;
import io.zaplink.manager.entity.UrlMappingEntity;
import io.zaplink.manager.repository.RedirectRuleRepository;
import io.zaplink.manager.repository.UrlAnalyticsRepository;
import io.zaplink.manager.repository.UrlMappingRepository;
import io.zaplink.manager.service.helper.RedisServiceHelper;
import io.zaplink.manager.service.url.UrlManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @Slf4j @RequiredArgsConstructor
public class UrlManagerServiceImpl
    implements
    UrlManagerService
{
    private final RedisServiceHelper     redisService;
    private final UrlMappingRepository   urlMappingRepository;
    private final UrlAnalyticsRepository urlAnalyticsRepository;
    private final RedirectRuleRepository redirectRuleRepository;
    @Override
    public List<LinkResponse> getLinksByUser( String userEmail )
    {
        return urlMappingRepository.findByUserEmailOrderByCreatedAtDesc( userEmail ).stream()
                .map( this::mapToLinkResponse ).collect( Collectors.toList() );
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
                .rules( mapToRuleDtos( entity.getId() ) ).build();
    }

    private List<RedirectRuleDto> mapToRuleDtos( Long urlMappingId )
    {
        List<RedirectRuleEntity> rules = redirectRuleRepository.findByUrlMappingIdOrderByPriorityDesc( urlMappingId );
        if ( rules == null || rules.isEmpty() )
            return null;
        return rules.stream()
                .map( r -> RedirectRuleDto.builder().dimension( r.getDimension() ).value( r.getValue() )
                        .destinationUrl( r.getDestinationUrl() ).priority( r.getPriority() ).build() )
                .collect( Collectors.toList() );
    }

    @Override
    public LinkAnalyticsResponse getLinkAnalytics( String shortUrlKey, String userEmail )
    {
        // 1. Verify link ownership
        // 1. Verify link ownership
        UrlMappingEntity link = urlMappingRepository.findByShortUrlKey( shortUrlKey )
                .orElseThrow( () -> new RuntimeException( "Link not found" ) );
        if ( !link.getUserEmail().equals( userEmail ) )
        {
            throw new RuntimeException( "Unauthorized access to link analytics" );
        }
        log.info( "Fetching analytics for key: {}", shortUrlKey );
        try
        {
            // 2. Fetch Aggregated Stats
            List<Object[]> countryStats = urlAnalyticsRepository.findTopCountriesByShortUrlKey( shortUrlKey );
            List<Object[]> browserStats = urlAnalyticsRepository.findTopBrowsersByShortUrlKey( shortUrlKey );
            List<Object[]> referrerStats = urlAnalyticsRepository.findTopReferrersByShortUrlKey( shortUrlKey );
            List<Object[]> dailyStats = urlAnalyticsRepository.findDailyClickCounts( shortUrlKey );
            // 3. Transform to DTO entries
            List<LinkAnalyticsResponse.Entry> countries = mapToEntries( countryStats, link.getClickCount() );
            List<LinkAnalyticsResponse.Entry> browsers = mapToEntries( browserStats, link.getClickCount() );
            List<LinkAnalyticsResponse.Entry> referrers = mapToEntries( referrerStats, link.getClickCount() );
            List<LinkAnalyticsResponse.Entry> dailyClicks = dailyStats.stream().map( obj -> {
                String dateStr = obj[0] != null ? obj[0].toString() : "Unknown";
                Long adCount = obj[1] instanceof Number ? ( (Number) obj[1] ).longValue() : 0L;
                return LinkAnalyticsResponse.Entry.builder().name( dateStr ).value( adCount ).build();
            } ).collect( Collectors.toList() );
            // 4. Build Response
            return LinkAnalyticsResponse.builder().shortUrlKey( link.getShortUrlKey() )
                    .originalUrl( link.getOriginalUrl() ).totalClicks( link.getClickCount() ).clicksToday( 0L )
                    .lastAccessed( null ).topCountries( countries ).topBrowsers( browsers ).topReferrers( referrers )
                    .dailyClicks( dailyClicks ).build();
        }
        catch ( Exception e )
        {
            log.error( "Error processing analytics for key: {}", shortUrlKey, e );
            throw e;
        }
    }

    private List<LinkAnalyticsResponse.Entry> mapToEntries( List<Object[]> data, Long total )
    {
        return data.stream().limit( 5 ).map( obj -> {
            String name = obj[0] != null ? String.valueOf( obj[0] ) : "Unknown";
            Long count = obj[1] instanceof Number ? ( (Number) obj[1] ).longValue() : 0L;
            return LinkAnalyticsResponse.Entry.builder().name( name ).value( count )
                    .percentage( (double) calculatePercentage( count, total ) ).build();
        } ).collect( Collectors.toList() );
    }
}
