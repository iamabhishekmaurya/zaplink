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
import io.zaplink.manager.dto.request.ClickCount;
import io.zaplink.manager.dto.response.LinkResponse;
import io.zaplink.manager.dto.response.StatsResponse;
import io.zaplink.manager.entity.UrlMappingEntity;
import io.zaplink.manager.repository.UrlMappingRepository;
import io.zaplink.manager.service.UrlManagerService;
import io.zaplink.manager.service.helper.KafkaServiceHelper;
import io.zaplink.manager.service.helper.RedisServiceHelper;
import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class UrlManagerServiceImpl
    implements
    UrlManagerService
{
    private final RedisServiceHelper   redisService;
    private final KafkaServiceHelper   kafkaService;
    private final UrlMappingRepository urlMappingRepository;
    public UrlManagerServiceImpl( RedisServiceHelper redisService,
                                  KafkaServiceHelper kafkaService,
                                  UrlMappingRepository urlMappingRepository )
    {
        this.redisService = redisService;
        this.kafkaService = kafkaService;
        this.urlMappingRepository = urlMappingRepository;
    }

    @Override
    public String getShortUrl( String key )
    {
        try
        {
            String originalUrl = redisService.getValue( key );
            if ( originalUrl != null && !originalUrl.isEmpty() )
            {
                log.info( LogConstants.LOG_URL_FOUND_IN_REDIS, key );
                // Async: Publish click event (fire-and-forget)
                publishClickEventAsync( key );
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
                        publishClickEventAsync( key );
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
        return StatsResponse.builder().totalLinks( totalLinks ).activeLinks( activeLinks )
                .totalClicks( totalClicks != null ? totalClicks : 0L ).build();
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
     * Publish click event (fire-and-forget)
     * @param key
     */
    @Async
    private void publishClickEventAsync( String key )
    {
        log.info( LogConstants.LOG_CLICK_EVENT_PUBLISHED, key );
        try
        {
            ClickCount clickCount = new ClickCount();
            clickCount.setUrlKey( key );
            clickCount.setCount( 1 );
            clickCount.setTraceId( TraceContext.getTraceId() );
            kafkaService.sendMessage( clickCount );
        }
        catch ( Exception ex )
        {
            log.error( LogConstants.LOG_EXCEPTION_PUBLISHING_CLICK_EVENT, key, ex.getMessage() );
        }
    }
}
