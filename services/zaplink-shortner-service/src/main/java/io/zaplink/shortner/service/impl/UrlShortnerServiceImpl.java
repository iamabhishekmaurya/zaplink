package io.zaplink.shortner.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.zaplink.shortner.dto.request.UrlConsumerRequest;
import io.zaplink.shortner.dto.request.ShortnerRequest;
import io.zaplink.shortner.dto.response.ShortnerResponse;
import io.zaplink.shortner.service.UrlShortnerService;
import io.zaplink.shortner.service.helper.KafkaServiceHelper;
import io.zaplink.shortner.service.helper.RedisServiceHelper;
import io.zaplink.shortner.utility.SnowflakeShortKeyGenerator;
import io.zaplink.shortner.utility.StringUtil;

@Service
public class UrlShortnerServiceImpl
    implements
    UrlShortnerService
{
    private static final Logger logger     = LoggerFactory.getLogger( UrlShortnerServiceImpl.class );
    private RedisServiceHelper  redisService;
    private KafkaServiceHelper  kafkaService;
    private static final String BASE_URL   = "http://localhost:8083/";
    private static final int    DAY_IN_SEC = 24 * 60 * 60;
    private static final String CACHE_NAME = "shortUrls";
    public UrlShortnerServiceImpl( RedisServiceHelper redisService, KafkaServiceHelper kafkaService )
    {
        this.redisService = redisService;
        this.kafkaService = kafkaService;
    }

    @Override
    // @CachePut(cacheNames = CACHE_NAME, key = "#result.key")
    public ShortnerResponse shortUrl( ShortnerRequest urlRequest )
    {
        logger.info( "Going to short the url." );
        ShortnerResponse shortUrlResponse = new ShortnerResponse();
        UrlConsumerRequest shortUrlConsumerRequest = new UrlConsumerRequest();
        try
        {
            SnowflakeShortKeyGenerator keyGenerator = new SnowflakeShortKeyGenerator( 0 );
            String key = keyGenerator.generateShortKey();
            String shortUrl = StringUtil.concatStrings( BASE_URL, key );
            shortUrlResponse.setUrl( shortUrl );
            shortUrlResponse.setTraceId( urlRequest.getTraceId() );
            shortUrlConsumerRequest.setTraceId( urlRequest.getTraceId() );
            shortUrlConsumerRequest.setShortUrlKey( key );
            shortUrlConsumerRequest.setOriginalUrl( urlRequest.getLongUrl() );
            kafkaService.sendMessage( shortUrlConsumerRequest );
            redisService.setValue( key, urlRequest.getLongUrl(), DAY_IN_SEC );
        }
        catch ( Exception e )
        {
            logger.error( "Exception while shorting the url. Error: {}", e.getMessage() );
        }
        return shortUrlResponse;
    }

    @Override
    // @Cacheable(cacheNames = CACHE_NAME, key = "#key")
    public ShortnerResponse getShortUrl( String key )
    {
        ShortnerResponse shortUrlResponse = new ShortnerResponse();
        try
        {
            String originalUrl = redisService.getValue( key );
            shortUrlResponse.setUrl( originalUrl );
            shortUrlResponse.setTraceId( "skdhfksdhfkhsdkfhksjd" );
        }
        catch ( Exception ex )
        {
            logger.error( "Exception while fetching url for key: {}. Error: {}", key, ex.getMessage() );
        }
        return shortUrlResponse;
    }
}
