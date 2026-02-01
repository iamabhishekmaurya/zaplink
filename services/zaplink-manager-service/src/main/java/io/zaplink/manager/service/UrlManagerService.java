package io.zaplink.manager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.zaplink.manager.common.enums.UrlStatusEnum;
import io.zaplink.manager.dto.response.LinkAnalyticsResponse;
import io.zaplink.manager.dto.response.LinkResponse;
import io.zaplink.manager.dto.response.StatsResponse;
import io.zaplink.manager.repository.UrlAnalyticsRepository;
import io.zaplink.manager.service.grpc.AuthGrpcClient;
import io.zaplink.manager.service.grpc.CoreGrpcClient;
import io.zaplink.manager.service.helper.RedisServiceHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @Slf4j @RequiredArgsConstructor
public class UrlManagerService
{
    private final RedisServiceHelper     redisService;
    private final UrlAnalyticsRepository urlAnalyticsRepository;
    private final CoreGrpcClient         coreGrpcClient;
    private final AuthGrpcClient         authGrpcClient;
    public List<LinkResponse> getLinksByUser( String userEmail )
    {
        log.info( "Going to get links by userEmail: {}", userEmail );
        List<LinkResponse> urlsList = coreGrpcClient.getUrlsByUser( userEmail );
        log.info( "Urls List: {}", urlsList );
        return urlsList;
    }

    public StatsResponse getUserStats( String userEmail )
    {
        // Get URLs from Core service via gRPC
        List<LinkResponse> userLinks = coreGrpcClient.getUrlsByUser( userEmail );
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

    public void deleteLink( Long id, String userEmail )
    {
        // Get the URL first to verify ownership
        LinkResponse link = coreGrpcClient.getUrlById( id.toString() );
        if ( link != null && link.shortUrl().contains( userEmail ) )
        {
            // For now, we'll need to implement a delete method in Core service
            // This is a placeholder - you'll need to add deleteUrl method to Core gRPC service
            log.info( "Deleting link {} for user {}", id, userEmail );
            redisService.deleteValue( link.shortUrlKey() );
        }
        else
        {
            throw new RuntimeException( "Link not found or unauthorized" );
        }
    }

    public LinkAnalyticsResponse getLinkAnalytics( String shortUrlKey, String userEmail )
    {
        // 1. Get link from Core service via gRPC
        List<LinkResponse> userLinks = coreGrpcClient.getUrlsByUser( userEmail );
        LinkResponse link = userLinks.stream().filter( l -> l.shortUrlKey().equals( shortUrlKey ) ).findFirst()
                .orElseThrow( () -> new RuntimeException( "Link not found" ) );
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
}
