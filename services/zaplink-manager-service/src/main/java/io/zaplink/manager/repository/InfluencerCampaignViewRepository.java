package io.zaplink.manager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.zaplink.manager.entity.InfluencerCampaignView;

/**
 * Repository for InfluencerCampaignView entity operations.
 * Provides read-optimized access to influencer campaign assignments.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Repository
public interface InfluencerCampaignViewRepository
    extends
    JpaRepository<InfluencerCampaignView, Long>
{
    /**
     * Find influencer campaign view by campaign ID.
     * 
     * @param campaignId The campaign ID
     * @return List of influencers assigned to the campaign
     */
    List<InfluencerCampaignView> findByCampaignId( Long campaignId );

    /**
     * Find influencer campaign view by team member ID.
     * 
     * @param teamMemberId The team member ID
     * @return List of campaigns assigned to the influencer
     */
    List<InfluencerCampaignView> findByTeamMemberId( Long teamMemberId );

    /**
     * Find influencer campaign view by team ID.
     * 
     * @param teamId The team ID
     * @return List of campaign assignments for the team
     */
    List<InfluencerCampaignView> findByTeamId( Long teamId );

    /**
     * Find influencer campaign view by organization ID.
     * 
     * @param organizationId The organization ID
     * @return List of campaign assignments in the organization
     */
    List<InfluencerCampaignView> findByOrganizationId( Long organizationId );

    /**
     * Find influencer campaign view by assignment status.
     * 
     * @param assignmentStatus The assignment status to search for
     * @return List of campaign assignments with the specified status
     */
    List<InfluencerCampaignView> findByAssignmentStatus( String assignmentStatus );

    /**
     * Find influencer campaign view by campaign status.
     * 
     * @param campaignStatus The campaign status to search for
     * @return List of campaign assignments for campaigns with the specified status
     */
    List<InfluencerCampaignView> findByCampaignStatus( String campaignStatus );

    /**
     * Find influencer campaign view by team member ID and assignment status.
     * 
     * @param teamMemberId The team member ID
     * @param assignmentStatus The assignment status to search for
     * @return List of campaign assignments for the influencer with the specified status
     */
    List<InfluencerCampaignView> findByTeamMemberIdAndAssignmentStatus( Long teamMemberId, String assignmentStatus );

    /**
     * Find influencer campaign view by organization ID and assignment status.
     * 
     * @param organizationId The organization ID
     * @param assignmentStatus The assignment status to search for
     * @return List of campaign assignments in the organization with the specified status
     */
    List<InfluencerCampaignView> findByOrganizationIdAndAssignmentStatus( Long organizationId, String assignmentStatus );

    /**
     * Find influencer campaign view by campaign ID and assignment status.
     * 
     * @param campaignId The campaign ID
     * @param assignmentStatus The assignment status to search for
     * @return List of influencers assigned to the campaign with the specified status
     */
    List<InfluencerCampaignView> findByCampaignIdAndAssignmentStatus( Long campaignId, String assignmentStatus );

    /**
     * Count influencer campaign assignments by campaign ID.
     * 
     * @param campaignId The campaign ID
     * @return Number of influencers assigned to the campaign
     */
    long countByCampaignId( Long campaignId );

    /**
     * Count influencer campaign assignments by team member ID.
     * 
     * @param teamMemberId The team member ID
     * @return Number of campaigns assigned to the influencer
     */
    long countByTeamMemberId( Long teamMemberId );

    /**
     * Count influencer campaign assignments by organization ID.
     * 
     * @param organizationId The organization ID
     * @return Number of campaign assignments in the organization
     */
    long countByOrganizationId( Long organizationId );

    /**
     * Find active campaign assignments for an influencer.
     * 
     * @param teamMemberId The team member ID
     * @return List of active campaign assignments for the influencer
     */
    @Query("SELECT icv FROM InfluencerCampaignView icv "
            + "WHERE icv.teamMemberId = :teamMemberId "
            + "AND icv.assignmentStatus = 'ASSIGNED' "
            + "AND icv.campaignStatus = 'ACTIVE'")
    List<InfluencerCampaignView> findActiveAssignmentsByInfluencer( @Param("teamMemberId") Long teamMemberId );

    /**
     * Find completed campaign assignments for an influencer.
     * 
     * @param teamMemberId The team member ID
     * @return List of completed campaign assignments for the influencer
     */
    @Query("SELECT icv FROM InfluencerCampaignView icv "
            + "WHERE icv.teamMemberId = :teamMemberId "
            + "AND icv.assignmentStatus = 'COMPLETED' "
            + "ORDER BY icv.completedAt DESC")
    List<InfluencerCampaignView> findCompletedAssignmentsByInfluencer( @Param("teamMemberId") Long teamMemberId );

    /**
     * Find campaign assignments within a date range.
     * 
     * @param organizationId The organization ID
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of campaign assignments within the date range
     */
    @Query("SELECT icv FROM InfluencerCampaignView icv "
            + "WHERE icv.organizationId = :organizationId "
            + "AND icv.assignedAt BETWEEN :startDate AND :endDate "
            + "ORDER BY icv.assignedAt DESC")
    List<InfluencerCampaignView> findByOrganizationIdAndAssignedAtBetween( @Param("organizationId") Long organizationId,
                                                                          @Param("startDate") java.time.Instant startDate,
                                                                          @Param("endDate") java.time.Instant endDate );
}
