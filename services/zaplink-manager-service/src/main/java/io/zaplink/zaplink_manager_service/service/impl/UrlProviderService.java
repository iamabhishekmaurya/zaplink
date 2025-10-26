package io.zaplink.zaplink_manager_service.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.zaplink.zaplink_manager_service.service.RedisService;
import io.zaplink.zaplink_manager_service.service.provider.UrlProvider;

@Service
public class UrlProviderService
    implements
    UrlProvider
{
    private Logger       logger = LoggerFactory.getLogger( UrlProviderService.class );
    private RedisService redisService;
    public UrlProviderService( RedisService redisService )
    {
        this.redisService = redisService;
    }

    @Override
    public String getShortUrl( String key )
    {
        try
        {
            String originalUrl = redisService.getValue( key );
            if ( !originalUrl.isEmpty() )
            {
                logger.info( "Url found for key: {}", key );
                return originalUrl;
            }
            else
            {
                logger.info( "Url not found for key: {}", key );
            }
        }
        catch ( Exception ex )
        {
            logger.error( "Exception while fetching url for key: {}. Error: {}", key, ex.getMessage() );
        }
        return null;
    }
}
