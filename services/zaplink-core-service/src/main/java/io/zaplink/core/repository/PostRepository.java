package io.zaplink.core.repository;

import io.zaplink.core.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Post entity operations.
 * Provides database access methods for post and workflow management.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    /**
     * Find all posts by author ID.
     * 
     * @param authorId The author ID
     * @return List of posts created by the author
     */
    List<Post> findByAuthorId(Long authorId);
    
    /**
     * Find all posts by status.
     * 
     * @param status The status to search for
     * @return List of posts with the specified status
     */
    List<Post> findByStatus(String status);
    
    /**
     * Find all posts by campaign ID.
     * 
     * @param campaignId The campaign ID
     * @return List of posts associated with the campaign
     */
    List<Post> findByCampaignId(Long campaignId);
    
    /**
     * Find all posts by author ID and status.
     * 
     * @param authorId The author ID
     * @param status The status to search for
     * @return List of posts by the author with the specified status
     */
    List<Post> findByAuthorIdAndStatus(Long authorId, String status);
    
    /**
     * Find all posts submitted for approval (status = SUBMITTED).
     * 
     * @return List of posts pending approval
     */
    List<Post> findByStatusOrderBySubmittedAtDesc(String status);
    
    /**
     * Find all posts by campaign ID and status.
     * 
     * @param campaignId The campaign ID
     * @param status The status to search for
     * @return List of posts in the campaign with the specified status
     */
    List<Post> findByCampaignIdAndStatus(Long campaignId, String status);
    
    /**
     * Count posts by author ID and status.
     * 
     * @param authorId The author ID
     * @param status The status to count
     * @return Number of posts by the author with the specified status
     */
    long countByAuthorIdAndStatus(Long authorId, String status);
    
    /**
     * Count posts by campaign ID and status.
     * 
     * @param campaignId The campaign ID
     * @param status The status to count
     * @return Number of posts in the campaign with the specified status
     */
    long countByCampaignIdAndStatus(Long campaignId, String status);
    
    /**
     * Find posts pending approval for a specific organization.
     * 
     * @param organizationId The organization ID
     * @return List of posts pending approval in the organization
     */
    @Query("SELECT p FROM Post p " +
           "JOIN TeamMember tm ON p.authorId = tm.userId " +
           "JOIN Team t ON tm.teamId = t.id " +
           "WHERE t.organizationId = :organizationId AND p.status = 'SUBMITTED' " +
           "ORDER BY p.submittedAt DESC")
    List<Post> findPendingApprovalByOrganization(@Param("organizationId") Long organizationId);
    
    /**
     * Find posts pending approval for a specific team.
     * 
     * @param teamId The team ID
     * @return List of posts pending approval in the team
     */
    @Query("SELECT p FROM Post p " +
           "JOIN TeamMember tm ON p.authorId = tm.userId " +
           "WHERE tm.teamId = :teamId AND p.status = 'SUBMITTED' " +
           "ORDER BY p.submittedAt DESC")
    List<Post> findPendingApprovalByTeam(@Param("teamId") Long teamId);
}
