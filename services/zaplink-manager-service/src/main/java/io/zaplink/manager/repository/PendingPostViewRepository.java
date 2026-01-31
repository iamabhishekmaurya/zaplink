package io.zaplink.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.zaplink.manager.entity.PendingPostView;

/**
 * Repository for PendingPostView entity operations.
 * Provides read-optimized access to posts pending approval.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Repository
public interface PendingPostViewRepository
       extends
       JpaRepository<PendingPostView, Long>
{
       /**
        * Find pending posts by organization ID.
        * 
        * @param organizationId The organization ID
        * @return List of pending posts in the organization
        */
       List<PendingPostView> findByOrganizationId( Long organizationId );

       /**
        * Find pending posts by team ID.
        * 
        * @param teamId The team ID
        * @return List of pending posts in the team
        */
       List<PendingPostView> findByTeamId( Long teamId );

       /**
        * Find pending posts by author ID.
        * 
        * @param authorId The author ID
        * @return List of pending posts by the author
        */
       List<PendingPostView> findByAuthorId( Long authorId );

       /**
        * Find pending posts by campaign ID.
        * 
        * @param campaignId The campaign ID
        * @return List of pending posts in the campaign
        */
       List<PendingPostView> findByCampaignId( Long campaignId );

       /**
        * Find pending posts by status.
        * 
        * @param status The status to search for
        * @return List of pending posts with the specified status
        */
       List<PendingPostView> findByStatus( String status );

       /**
        * Find pending posts by organization ID ordered by submission date.
        * 
        * @param organizationId The organization ID
        * @return List of pending posts ordered by submission date (newest first)
        */
       List<PendingPostView> findByOrganizationIdOrderBySubmittedAtDesc( Long organizationId );

       /**
        * Find pending posts by team ID ordered by submission date.
        * 
        * @param teamId The team ID
        * @return List of pending posts ordered by submission date (newest first)
        */
       List<PendingPostView> findByTeamIdOrderBySubmittedAtDesc( Long teamId );

       /**
        * Count pending posts by organization ID.
        * 
        * @param organizationId The organization ID
        * @return Number of pending posts in the organization
        */
       long countByOrganizationId( Long organizationId );

       /**
        * Count pending posts by team ID.
        * 
        * @param teamId The team ID
        * @return Number of pending posts in the team
        */
       long countByTeamId( Long teamId );

       /**
        * Count pending posts by author ID.
        * 
        * @param authorId The author ID
        * @return Number of pending posts by the author
        */
       long countByAuthorId( Long authorId );

       /**
        * Find pending posts submitted within a date range.
        * 
        * @param organizationId The organization ID
        * @param startDate Start date of the range
        * @param endDate End date of the range
        * @return List of pending posts submitted within the date range
        */
       @Query("SELECT ppv FROM PendingPostView ppv " + "WHERE ppv.organizationId = :organizationId "
                     + "AND ppv.submittedAt BETWEEN :startDate AND :endDate " + "ORDER BY ppv.submittedAt DESC")
       List<PendingPostView> findByOrganizationIdAndSubmittedAtBetween( @Param("organizationId") Long organizationId,
                                                                        @Param("startDate") java.time.Instant startDate,
                                                                        @Param("endDate") java.time.Instant endDate );
}
