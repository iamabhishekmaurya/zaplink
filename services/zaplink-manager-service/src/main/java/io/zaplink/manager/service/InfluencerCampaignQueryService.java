// package io.zaplink.manager.service;
// import java.util.List;
// import org.springframework.stereotype.Service;
// import io.zaplink.manager.entity.InfluencerCampaignView;
// import io.zaplink.manager.repository.InfluencerCampaignViewRepository;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// /**
//  * Service for querying influencer campaign read model data.
//  * Provides business logic for accessing influencer campaign information.
//  * 
//  * @author Zaplink Team
//  * @version 1.0
//  * @since 2026-01-31
//  */
// @Slf4j @Service @RequiredArgsConstructor
// public class InfluencerCampaignQueryService
// {
//     private final InfluencerCampaignViewRepository influencerCampaignViewRepository;
//     /**
//      * Get all campaign assignments for an influencer.
//      * 
//      * @param teamMemberId The team member ID
//      * @return List of campaign assignments
//      */
//     public List<InfluencerCampaignView> getInfluencerCampaigns( Long teamMemberId )
//     {
//         log.debug( "Fetching campaigns for influencer: {}", teamMemberId );
//         return influencerCampaignViewRepository.findByTeamMemberId( teamMemberId );
//     }
//     /**
//      * Get active campaign assignments for an influencer.
//      * 
//      * @param teamMemberId The team member ID
//      * @return List of active campaign assignments
//      */
//     public List<InfluencerCampaignView> getActiveInfluencerCampaigns( Long teamMemberId )
//     {
//         log.debug( "Fetching active campaigns for influencer: {}", teamMemberId );
//         return influencerCampaignViewRepository.findActiveAssignmentsByInfluencer( teamMemberId );
//     }
//     /**
//      * Get completed campaign assignments for an influencer.
//      * 
//      * @param teamMemberId The team member ID
//      * @return List of completed campaign assignments
//      */
//     public List<InfluencerCampaignView> getCompletedInfluencerCampaigns( Long teamMemberId )
//     {
//         log.debug( "Fetching completed campaigns for influencer: {}", teamMemberId );
//         return influencerCampaignViewRepository.findCompletedAssignmentsByInfluencer( teamMemberId );
//     }
//     /**
//      * Get all influencers assigned to a campaign.
//      * 
//      * @param campaignId The campaign ID
//      * @return List of influencers assigned to the campaign
//      */
//     public List<InfluencerCampaignView> getCampaignInfluencers( Long campaignId )
//     {
//         log.debug( "Fetching influencers for campaign: {}", campaignId );
//         return influencerCampaignViewRepository.findByCampaignId( campaignId );
//     }
//     /**
//      * Get campaign assignments by organization.
//      * 
//      * @param organizationId The organization ID
//      * @return List of campaign assignments in the organization
//      */
//     public List<InfluencerCampaignView> getOrganizationCampaignAssignments( Long organizationId )
//     {
//         log.debug( "Fetching campaign assignments for organization: {}", organizationId );
//         return influencerCampaignViewRepository.findByOrganizationId( organizationId );
//     }
//     /**
//      * Get campaign assignments by status.
//      * 
//      * @param assignmentStatus The assignment status
//      * @return List of campaign assignments with the specified status
//      */
//     public List<InfluencerCampaignView> getCampaignAssignmentsByStatus( String assignmentStatus )
//     {
//         log.debug( "Fetching campaign assignments by status: {}", assignmentStatus );
//         return influencerCampaignViewRepository.findByAssignmentStatus( assignmentStatus );
//     }
//     /**
//      * Count campaign assignments for an influencer.
//      * 
//      * @param teamMemberId The team member ID
//      * @return Number of campaign assignments
//      */
//     public long countInfluencerCampaigns( Long teamMemberId )
//     {
//         log.debug( "Counting campaigns for influencer: {}", teamMemberId );
//         return influencerCampaignViewRepository.countByTeamMemberId( teamMemberId );
//     }
//     /**
//      * Count influencers assigned to a campaign.
//      * 
//      * @param campaignId The campaign ID
//      * @return Number of influencers assigned
//      */
//     public long countCampaignInfluencers( Long campaignId )
//     {
//         log.debug( "Counting influencers for campaign: {}", campaignId );
//         return influencerCampaignViewRepository.countByCampaignId( campaignId );
//     }
//     /**
//      * Count campaign assignments in an organization.
//      * 
//      * @param organizationId The organization ID
//      * @return Number of campaign assignments
//      */
//     public long countOrganizationCampaignAssignments( Long organizationId )
//     {
//         log.debug( "Counting campaign assignments for organization: {}", organizationId );
//         return influencerCampaignViewRepository.countByOrganizationId( organizationId );
//     }
// }
