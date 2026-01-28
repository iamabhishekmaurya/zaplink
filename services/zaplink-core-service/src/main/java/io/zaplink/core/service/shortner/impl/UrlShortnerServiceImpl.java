package io.zaplink.core.service.shortner.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.zaplink.core.common.constants.LogConstants;
import io.zaplink.core.common.enums.UrlStatusEnum;
import io.zaplink.core.dto.request.ShortnerRequest;
import io.zaplink.core.dto.response.ShortnerResponse;
import io.zaplink.core.entity.RedirectRuleEntity;
import io.zaplink.core.entity.UrlMappingEntity;
import io.zaplink.core.repository.RedirectRuleRepository;
import io.zaplink.core.repository.UrlMappingRepository;
import io.zaplink.core.service.shortner.UrlShortnerService;
import io.zaplink.core.utility.SnowflakeShortKeyGenerator;
import io.zaplink.core.utility.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @Slf4j @RequiredArgsConstructor
public class UrlShortnerServiceImpl
    implements
    UrlShortnerService
{
    @Value("${redirect.base.url}")
    private String                       BASE_URL;
    private final UrlMappingRepository   urlMappingRepository;
    private final RedirectRuleRepository redirectRuleRepository;
    @Override
    public ShortnerResponse createShortUrl( ShortnerRequest urlRequest, String userEmail )
    {
        log.info( LogConstants.LOG_SHORT_URL_INIT );
        try
        {
            SnowflakeShortKeyGenerator keyGenerator = new SnowflakeShortKeyGenerator( 0 );
            String key = keyGenerator.generateShortKey();
            String shortUrl = StringUtil.concatStrings( BASE_URL, key );
            UrlMappingEntity urlMappingEntity = new UrlMappingEntity();
            createUrlMappingEntity( urlMappingEntity, key, shortUrl, urlRequest, userEmail );
            UrlMappingEntity savedUrlMappingEntity = urlMappingRepository.save( urlMappingEntity );
            if ( savedUrlMappingEntity != null )
            {
                if ( urlRequest.rules() != null && !urlRequest.rules().isEmpty() )
                {
                    final Long mappingId = savedUrlMappingEntity.getId();
                    List<RedirectRuleEntity> ruleEntities = urlRequest.rules().stream()
                            .map( r -> RedirectRuleEntity.builder().urlMappingId( mappingId ).dimension( r.dimension() )
                                    .value( r.value() ).destinationUrl( r.destinationUrl() ).priority( r.priority() )
                                    .createdAt( LocalDateTime.now() ).build() )
                            .toList();
                    redirectRuleRepository.saveAll( ruleEntities );
                }
                log.info( LogConstants.LOG_URL_MAPPING_CREATED );
                return new ShortnerResponse( savedUrlMappingEntity.getShortUrl(), savedUrlMappingEntity.getTraceId() );
            }
            else
            {
                log.error( LogConstants.LOG_URL_MAPPING_NOT_CREATED );
                return new ShortnerResponse( null, null );
            }
        }
        catch ( Exception ex )
        {
            log.error( LogConstants.LOG_URL_SHORTENING_EXCEPTION, ex.getMessage() );
            throw ex;
        }
    }

    /**
     * Creates a new URL mapping entity with the provided parameters.
     * 
     * @param urlMappingEntity the URL mapping entity to create
     * @param key              the short URL key
     * @param shortUrl         the short URL
     * @param urlRequest       the original URL request
     */
    private void createUrlMappingEntity( UrlMappingEntity urlMappingEntity,
                                         String key,
                                         String shortUrl,
                                         ShortnerRequest urlRequest,
                                         String userEmail )
    {
        log.info( LogConstants.LOG_CREATING_URL_MAPPING, key );
        urlMappingEntity.setShortUrlKey( key );
        urlMappingEntity.setOriginalUrl( urlRequest.originalUrl() );
        urlMappingEntity.setShortUrl( shortUrl );
        urlMappingEntity.setUserEmail( userEmail );
        urlMappingEntity.setTraceId( urlRequest.traceId() );
        urlMappingEntity.setTitle( urlRequest.title() );
        urlMappingEntity.setPlatform( urlRequest.platform() );
        if ( urlRequest.tags() != null && !urlRequest.tags().isEmpty() )
        {
            urlMappingEntity.setTags( String.join( ",", urlRequest.tags() ) );
        }
        urlMappingEntity.setCreatedAt( LocalDateTime.now() );
        urlMappingEntity.setExpiresAt( LocalDateTime.now().plusSeconds( 60 * 60 * 24 * 15 ) );
        urlMappingEntity.setClickCount( 0L );
        urlMappingEntity.setStatus( UrlStatusEnum.ACTIVE );
    }
}
