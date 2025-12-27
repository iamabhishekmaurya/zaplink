package io.zaplink.manager.service;

import org.springframework.stereotype.Service;

@Service
public interface UrlManagerService
{
    /**
     * Get short url by key
     * @implNote Check if url is present in redis
     * @implNote If url is present in redis, return url
     * @implNote If url is not present in redis, check if url is present in database
     * @implNote If url is present in database, return url
     * @implNote If url is not present in database, return null
     * @param key
     * @return short url
     */
    String getShortUrl( String key );
}
