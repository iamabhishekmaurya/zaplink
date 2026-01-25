package io.zaplink.redirect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.zaplink.redirect.entity.UrlMappingEntity;

/**
 * Repository for URL mapping lookups.
 * Used for redirect resolution when cache miss occurs.
 */
@Repository
public interface UrlMappingRepository
    extends
    JpaRepository<UrlMappingEntity, Long>
{
    /**
     * Find URL mapping by short URL key.
     * Primary method for redirect resolution.
     *
     * @param shortUrlKey the short URL key
     * @return Optional containing the URL mapping if found
     */
    Optional<UrlMappingEntity> findByShortUrlKey( String shortUrlKey );
}
