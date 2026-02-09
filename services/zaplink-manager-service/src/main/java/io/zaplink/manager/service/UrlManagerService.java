package io.zaplink.manager.service;

import java.util.Collections;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @Slf4j @RequiredArgsConstructor
public class UrlManagerService
{
    private final UrlAnalyticsRepository urlAnalyticsRepository;
    private final UrlMappingRepository   urlMappingRepository;
    private final RedirectRuleRepository redirectRuleRepository;
    
    public List<LinkResponse> getLinksByUser( String userEmail )
    {
        log.info( "Going to get links by userEmail: {}", userEmail );
        List<UrlMappingEntity> entities = urlMappingRepository.findByUserEmailOrderByCreatedAtDesc( userEmail );
        List<LinkResponse> urlsList = entities.stream().map( this::mapToLinkResponse ).collect( Collectors.toList() );
        log.info( "Urls List size: {}", urlsList.size() );
        return urlsList;
    }

    public LinkResponse getLinkById( String id, String userEmail )
    {
        log.info( "Going to get link by id: {} for user: {}", id, userEmail );
        UrlMappingEntity entity = urlMappingRepository.findByIdAndUserEmail( Long.parseLong(id), userEmail )
                .orElseThrow( () -> new RuntimeException( "Link not found with id: " + id ) );
        
        List<RedirectRuleEntity> rules = redirectRuleRepository.findByUrlMappingIdOrderByPriorityDesc( entity.getId() );
        LinkResponse link = mapToLinkResponse( entity, rules );
        log.info( "Found link: {}", link.shortUrlKey() );
        return link;
    }

    public StatsResponse getUserStats( String userEmail )
    {
        // Get URLs directly from DB
        List<UrlMappingEntity> userEntities = urlMappingRepository.findByUserEmailOrderByCreatedAtDesc( userEmail );
        List<LinkResponse> userLinks = userEntities.stream().map( this::mapToLinkResponse )
                .collect( Collectors.toList() );
        long totalLinks = userLinks.size();
        long activeLinks = userLinks.stream().mapToLong( link -> link.status().equals( UrlStatusEnum.ACTIVE ) ? 1 : 0 )
                .sum();
        Long totalClicks = userLinks.stream().mapToLong( LinkResponse::clickCount ).sum();
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
                                  totalClicks != null ? totalClicks : 0L,
                                  activeLinks,
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

    public LinkAnalyticsResponse getLinkAnalytics( String shortUrlKey, String userEmail )
    {
        // 1. Get link directly from DB
        UrlMappingEntity entity = urlMappingRepository.findByShortUrlKey( shortUrlKey )
                .orElseThrow( () -> new RuntimeException( "Link not found" ) );
        LinkResponse link = mapToLinkResponse( entity );
        log.info( "Fetching analytics for key: {}", shortUrlKey );
        try
        {
            // 2. Fetch Aggregated Stats
            List<Object[]> countryStats = urlAnalyticsRepository.findTopCountriesByShortUrlKey( shortUrlKey );
            List<Object[]> browserStats = urlAnalyticsRepository.findTopBrowsersByShortUrlKey( shortUrlKey );
            List<Object[]> referrerStats = urlAnalyticsRepository.findTopReferrersByShortUrlKey( shortUrlKey );
            List<Object[]> dailyStats = urlAnalyticsRepository.findDailyClickCounts( shortUrlKey );
            // 3. Transform to DTO entries
            List<LinkAnalyticsResponse.Entry> countries = mapToEntries( countryStats, link.clickCount() );
            List<LinkAnalyticsResponse.Entry> browsers = mapToEntries( browserStats, link.clickCount() );
            List<LinkAnalyticsResponse.Entry> referrers = mapToEntries( referrerStats, link.clickCount() );
            List<LinkAnalyticsResponse.Entry> dailyClicks = dailyStats.stream().map( obj -> {
                String dateStr = obj[0] != null ? obj[0].toString() : "Unknown";
                Long adCount = obj[1] instanceof Number ? ( (Number) obj[1] ).longValue() : 0L;
                return new LinkAnalyticsResponse.Entry( dateStr, adCount, null );
            } ).collect( Collectors.toList() );
            // 4. Build Response
            return new LinkAnalyticsResponse( link.shortUrlKey(),
                                              link.originalUrl(),
                                              link.clickCount(),
                                              0L,
                                              null,
                                              countries,
                                              browsers,
                                              referrers,
                                              dailyClicks );
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

    private LinkResponse mapToLinkResponse( UrlMappingEntity entity )
    {
        return mapToLinkResponse( entity, Collections.emptyList() );
    }

    private LinkResponse mapToLinkResponse( UrlMappingEntity entity, List<RedirectRuleEntity> rules )
    {
        List<RedirectRuleDto> ruleDtos = rules.stream().map( rule -> 
            new RedirectRuleDto( rule.getDimension(), rule.getValue(), rule.getDestinationUrl(), rule.getPriority() ) 
        ).collect( Collectors.toList() );
        return new LinkResponse( entity.getId(),
                                 entity.getShortUrlKey(),
                                 entity.getOriginalUrl(),
                                 entity.getShortUrl(),
                                 entity.getCreatedAt(),
                                 entity.getClickCount(),
                                 entity.getStatus(),
                                 ruleDtos, // Use the mapped rules instead of empty list
                                 entity.getTags() != null ? entity.getTags() : Collections.emptyList() );
    }
}
