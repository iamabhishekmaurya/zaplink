package io.zaplink.manager.service;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @Slf4j @RequiredArgsConstructor
public class UrlManagerService
{
    private final RedisServiceHelper     redisService;
    private final UrlMappingRepository   urlMappingRepository;
    private final UrlAnalyticsRepository urlAnalyticsRepository;
    private final RedirectRuleRepository redirectRuleRepository;
    public List<LinkResponse> getLinksByUser( String userEmail )
    {
        return urlMappingRepository.findByUserEmailOrderByCreatedAtDesc( userEmail ).stream()
                .map( this::mapToLinkResponse ).collect( Collectors.toList() );
    }

    public StatsResponse getUserStats( String userEmail )
    {
        long totalLinks = urlMappingRepository.countByUserEmail( userEmail );
        long activeLinks = urlMappingRepository.countByStatusAndUserEmail( UrlStatusEnum.ACTIVE, userEmail );
        Long totalClicks = urlMappingRepository.getTotalClickCountByUserEmail( userEmail );
        List<StatsResponse.Entry> clickTrend = urlAnalyticsRepository.findClickTrendByUser( userEmail ).stream()
                .map( obj -> new StatsResponse.Entry( obj[0].toString(), obj[1] ) ).collect( Collectors.toList() );
        List<StatsResponse.Entry> referrers = urlAnalyticsRepository.findTopReferrersByUser( userEmail ).stream()
                .limit( 5 )
                .map( obj -> new StatsResponse.Entry( (String) obj[0],
                                                      calculatePercentage( (Long) obj[1], totalClicks ) ) )
                .collect( Collectors.toList() );
        List<Object[]> regions = urlAnalyticsRepository.findTopRegionsByUser( userEmail );
        String topRegion = regions.isEmpty() ? "N/A" : (String) regions.get( 0 )[0];
        // Format referrers to have int value as percentage
        return new StatsResponse( totalLinks,
                                  activeLinks,
                                  totalClicks != null ? totalClicks : 0L,
                                  topRegion,
                                  0.0,
                                  clickTrend,
                                  referrers );
    }

    private int calculatePercentage( Long value, Long total )
    {
        if ( total == null || total == 0 )
            return 0;
        return (int) ( ( value * 100 ) / total );
    }

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
        return new LinkResponse( entity.getId(),
                                 entity.getShortUrlKey(),
                                 entity.getOriginalUrl(),
                                 entity.getShortUrl(),
                                 entity.getCreatedAt(),
                                 entity.getClickCount(),
                                 entity.getStatus(),
                                 mapToRuleDtos( entity.getId() ),
                                 entity.getTags() );
    }

    private List<RedirectRuleDto> mapToRuleDtos( Long urlMappingId )
    {
        List<RedirectRuleEntity> rules = redirectRuleRepository.findByUrlMappingIdOrderByPriorityDesc( urlMappingId );
        if ( rules == null || rules.isEmpty() )
            return null;
        return rules.stream()
                .map( r -> new RedirectRuleDto( r.getDimension(),
                                                r.getValue(),
                                                r.getDestinationUrl(),
                                                r.getPriority() ) )
                .collect( Collectors.toList() );
    }

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
                return new LinkAnalyticsResponse.Entry( dateStr, adCount, null );
            } ).collect( Collectors.toList() );
            // 4. Build Response
            return new LinkAnalyticsResponse( link.getShortUrlKey(),
                    link.getOriginalUrl(), link.getClickCount(), 0L,
                    null, countries, browsers, referrers, dailyClicks );
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
            return new LinkAnalyticsResponse.Entry( name, count, (double) calculatePercentage( count, total ) );
        } ).collect( Collectors.toList() );
    }
}
