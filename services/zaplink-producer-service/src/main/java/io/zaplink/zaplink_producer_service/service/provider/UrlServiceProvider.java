package io.zaplink.zaplink_producer_service.service.provider;

import io.zaplink.zaplink_producer_service.dto.request.ShortUrlRequest;
import io.zaplink.zaplink_producer_service.dto.response.ShortUrlResponse;

public interface UrlServiceProvider
{
    ShortUrlResponse shortUrl( ShortUrlRequest urlRequest );

    ShortUrlResponse getShortUrl( String key );
}
