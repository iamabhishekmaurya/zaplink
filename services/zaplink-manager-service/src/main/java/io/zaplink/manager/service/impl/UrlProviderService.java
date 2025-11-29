package io.zaplink.manager.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.zaplink.manager.service.UrlProvider;
import io.zaplink.manager.service.helper.RedisServiceHelper;

@Service
public class UrlProviderService
    implements
    UrlProvider
{
    private Logger       logger = LoggerFactory.getLogger( UrlProviderService.class );
    private RedisServiceHelper redisService;
    public UrlProviderService( RedisServiceHelper redisService )
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
