package io.zaplink.core.service.shortner.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.zaplink.core.common.constants.LogConstants;
import io.zaplink.core.common.enums.UrlStatusEnum;
import io.zaplink.core.dto.request.ShortnerRequest;
import io.zaplink.core.dto.response.ShortnerResponse;
import io.zaplink.core.entity.UrlMappingEntity;
import io.zaplink.core.repository.UrlMappingRepository;
import io.zaplink.core.service.shortner.UrlShortnerService;
import io.zaplink.core.utility.SnowflakeShortKeyGenerator;
import io.zaplink.core.utility.StringUtil;
import lombok.extern.slf4j.Slf4j;

@Service @Slf4j
public class UrlShortnerServiceImpl
    implements
    UrlShortnerService
{
    @Value("${base.url}")
    private String               BASE_URL;
    private UrlMappingRepository urlMappingRepository;
    public UrlShortnerServiceImpl( UrlMappingRepository urlMappingRepository )
    {
        this.urlMappingRepository = urlMappingRepository;
    }

    @Override
    public ShortnerResponse shortUrl( ShortnerRequest urlRequest, String userEmail )
    {
        log.info( LogConstants.LOG_SHORT_URL_INIT );
        ShortnerResponse shortUrlResponse = new ShortnerResponse();
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
                log.info( LogConstants.LOG_URL_MAPPING_CREATED );
                shortUrlResponse.setUrl( savedUrlMappingEntity.getShortUrl() );
                shortUrlResponse.setTraceId( savedUrlMappingEntity.getTraceId() );
            }
            else
            {
                log.error( LogConstants.LOG_URL_MAPPING_NOT_CREATED );
            }
        }
        catch ( Exception ex )
        {
            log.error( LogConstants.LOG_URL_SHORTENING_EXCEPTION, ex.getMessage() );
            throw ex;
        }
        return shortUrlResponse;
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
        urlMappingEntity.setOriginalUrl( urlRequest.getOriginalUrl() );
        urlMappingEntity.setShortUrl( shortUrl );
        urlMappingEntity.setUserEmail( userEmail );
        urlMappingEntity.setTraceId( urlRequest.getTraceId() );
        urlMappingEntity.setCreatedAt( LocalDateTime.now() );
        urlMappingEntity.setExpiresAt( LocalDateTime.now().plusSeconds( 60 * 60 * 24 * 15 ) );
        urlMappingEntity.setClickCount( 0L );
        urlMappingEntity.setStatus( UrlStatusEnum.ACTIVE );
    }
}
