package io.zaplink.manager.service;

import java.util.List;
import org.springframework.stereotype.Service;
import io.zaplink.manager.dto.response.LinkResponse;
import io.zaplink.manager.dto.response.StatsResponse;

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

    /**
     * Get links by user email
     * @param userEmail
     * @return list of links
     */
    List<LinkResponse> getLinksByUser( String userEmail );

    /**
     * Get user statistics
     * @param userEmail
     * @return statistics
     */
    StatsResponse getUserStats( String userEmail );

    /**
     * Delete link by id
     * @param id
     * @param userEmail
     */
    void deleteLink( Long id, String userEmail );
}
