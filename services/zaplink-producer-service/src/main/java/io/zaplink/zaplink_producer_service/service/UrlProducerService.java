package io.zaplink.zaplink_producer_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.zaplink.zaplink_producer_service.dto.request.ShortUrlConsumerRequest;
import io.zaplink.zaplink_producer_service.dto.request.ShortUrlRequest;
import io.zaplink.zaplink_producer_service.dto.response.ShortUrlResponse;
import io.zaplink.zaplink_producer_service.service.provider.UrlServiceProvider;
import io.zaplink.zaplink_producer_service.utility.SnowflakeShortKeyGenerator;
import io.zaplink.zaplink_producer_service.utility.StringUtil;

@Service
public class UrlProducerService
    implements
    UrlServiceProvider
{
    private static final Logger logger     = LoggerFactory.getLogger( UrlProducerService.class );
    private RedisService        redisService;
    private KafkaService        kafkaService;
    private static final String BASE_URL   = "http://localhost:8081/";
    private static final int    DAY_IN_SEC = 24 * 60 * 60;
    public UrlProducerService( RedisService redisService, KafkaService kafkaService )
    {
        this.redisService = redisService;
        this.kafkaService = kafkaService;
    }

    public ShortUrlResponse shortUrl( ShortUrlRequest urlRequest )
    {
        logger.info( "Going to short the url." );
        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();
        ShortUrlConsumerRequest shortUrlConsumerRequest = new ShortUrlConsumerRequest();
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
    public ShortUrlResponse getShortUrl( String key )
    {
        ShortUrlResponse shortUrlResponse = new ShortUrlResponse();
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
