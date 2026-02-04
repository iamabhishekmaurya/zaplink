package io.zaplink.manager.service;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zaplink.manager.dto.event.CampaignAssignmentEvent;
import io.zaplink.manager.dto.event.TeamMemberAddedEvent;
import io.zaplink.manager.dto.event.WorkflowStatusChangedEvent;
import io.zaplink.manager.entity.InfluencerCampaignView;
import io.zaplink.manager.entity.PendingPostView;
import io.zaplink.manager.entity.TeamMemberView;
import io.zaplink.manager.repository.InfluencerCampaignViewRepository;
import io.zaplink.manager.repository.PendingPostViewRepository;
import io.zaplink.manager.repository.TeamMemberViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for consuming Kafka events from Core Service.
 * Updates the Manager Service read model based on team, workflow, and campaign events.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Slf4j @Service @RequiredArgsConstructor
public class EventConsumerService
{
    private final TeamMemberViewRepository              teamMemberViewRepository;
    private final PendingPostViewRepository             pendingPostViewRepository;
    private final InfluencerCampaignViewRepository      influencerCampaignViewRepository;
    // Topic names
    private static final String                         TEAM_EVENTS_TOPIC     = "team-events";
    private static final String                         WORKFLOW_EVENTS_TOPIC = "workflow-events";
    private static final String                         CAMPAIGN_EVENTS_TOPIC = "campaign-events";
    /**
     * Consumes team member added events from Kafka.
     * Updates the team member read model.
     * 
     * @param event The team member added event
     */
    @KafkaListener(topics = TEAM_EVENTS_TOPIC, groupId = "manager-service") @Transactional
    public void handleTeamMemberAddedEvent( TeamMemberAddedEvent event )
    {
        try
        {
            log.info( "Processing team member added event: {}", event.eventId() );
            // Check if record already exists
            if ( teamMemberViewRepository.existsById( event.teamMemberId() ) )
            {
                log.warn( "Team member view record already exists: {}", event.teamMemberId() );
                return;
            }
            // Create team member view record
            TeamMemberView teamMemberView = TeamMemberView.builder().id( event.teamMemberId() ).teamId( event.teamId() )
                    .teamName( event.teamName() ).userId( event.userId() ).username( event.username() )
                    .userEmail( event.email() ).firstName( event.firstName() ).lastName( event.lastName() )
                    .role( event.role() ).status( event.status() ).invitedAt( event.invitedAt() ).joinedAt( null ) // Will be updated when user accepts invitation
                    .organizationId( event.organizationId() ).organizationName( event.organizationName() )
                    .lastUpdated( Instant.now() ).build();
            teamMemberViewRepository.save( teamMemberView );
            log.info( "Team member view record created successfully: {}", event.teamMemberId() );
        }
        catch ( Exception ex )
        {
            log.error( "Error processing team member added event: {}", event.eventId(), ex );
            throw new RuntimeException( "Failed to process team member added event", ex );
        }
    }

    /**
     * Consumes workflow status changed events from Kafka.
     * Updates the pending post read model.
     * 
     * @param event The workflow status changed event
     */
    @KafkaListener(topics = WORKFLOW_EVENTS_TOPIC, groupId = "manager-service") @Transactional
    public void handleWorkflowStatusChangedEvent( WorkflowStatusChangedEvent event )
    {
        try
        {
            log.info( "Processing workflow status changed event: {}", event.eventId() );
            // Handle different status transitions
            switch ( event.newStatus() )
            {
                case "SUBMITTED":
                    // Add to pending post view
                    addToPendingPostView( event );
                    break;
                case "APPROVED":
                case "REJECTED":
                    // Remove from pending post view
                    removeFromPendingPostView( event.postId() );
                    break;
                default:
                    log.warn( "Unhandled workflow status: {}", event.newStatus() );
            }
            log.info( "Workflow status changed event processed successfully: {}", event.eventId() );
        }
        catch ( Exception ex )
        {
            log.error( "Error processing workflow status changed event: {}", event.eventId(), ex );
            throw new RuntimeException( "Failed to process workflow status changed event", ex );
        }
    }

    /**
     * Adds a post to the pending post view.
     * 
     * @param event The workflow status changed event
     */
    private void addToPendingPostView( WorkflowStatusChangedEvent event )
    {
        // Check if record already exists
        if ( pendingPostViewRepository.existsById( event.postId() ) )
        {
            log.warn( "Pending post view record already exists: {}", event.postId() );
            return;
        }
        // Create pending post view record
        PendingPostView pendingPostView = PendingPostView.builder().id( event.postId() ).title( event.title() )
                .content( null ) // Content not included in event for security
                .authorId( event.authorId() ).authorName( event.authorName() ).authorEmail( event.authorEmail() )
                .campaignId( event.campaignId() ).campaignName( event.campaignName() ).teamId( event.teamId() )
                .teamName( event.teamName() ).organizationId( event.organizationId() )
                .organizationName( event.organizationName() ).submittedAt( event.changedAt() )
                .status( event.newStatus() ).lastUpdated( Instant.now() ).build();
        pendingPostViewRepository.save( pendingPostView );
        log.info( "Pending post view record created successfully: {}", event.postId() );
    }

    /**
     * Removes a post from the pending post view.
     * 
     * @param postId The post ID to remove
     */
    private void removeFromPendingPostView( Long postId )
    {
        if ( !pendingPostViewRepository.existsById( postId ) )
        {
            log.warn( "Pending post view record not found: {}", postId );
            return;
        }
        pendingPostViewRepository.deleteById( postId );
        log.info( "Pending post view record removed successfully: {}", postId );
    }

    /**
     * Consumes campaign assignment events from Kafka.
     * Updates the influencer campaign read model.
     * 
     * @param event The campaign assignment event
     */
    @KafkaListener(topics = CAMPAIGN_EVENTS_TOPIC, groupId = "manager-service") @Transactional
    public void handleCampaignAssignmentEvent( CampaignAssignmentEvent event )
    {
        try
        {
            log.info( "Processing campaign assignment event: {}", event.eventId() );
            // Handle different assignment status transitions
            switch ( event.assignmentStatus() )
            {
                case "ASSIGNED":
                    // Add to influencer campaign view
                    addToInfluencerCampaignView( event );
                    break;
                case "COMPLETED":
                    // Update completion status
                    updateCampaignAssignmentCompleted( event );
                    break;
                case "CANCELLED":
                    // Remove from influencer campaign view
                    removeFromInfluencerCampaignView( event.assignmentId() );
                    break;
                default:
                    log.warn( "Unhandled campaign assignment status: {}", event.assignmentStatus() );
            }
            log.info( "Campaign assignment event processed successfully: {}", event.eventId() );
        }
        catch ( Exception ex )
        {
            log.error( "Error processing campaign assignment event: {}", event.eventId(), ex );
            throw new RuntimeException( "Failed to process campaign assignment event", ex );
        }
    }

    /**
     * Adds a campaign assignment to the influencer campaign view.
     * 
     * @param event The campaign assignment event
     */
    private void addToInfluencerCampaignView( CampaignAssignmentEvent event )
    {
        // Check if record already exists
        if ( influencerCampaignViewRepository.existsById( event.assignmentId() ) )
        {
            log.warn( "Influencer campaign view record already exists: {}", event.assignmentId() );
            return;
        }
        // Create influencer campaign view record
        InfluencerCampaignView influencerCampaignView = InfluencerCampaignView.builder()
                .id( event.assignmentId() ).campaignId( event.campaignId() ).campaignName( event.campaignName() )
                .campaignDescription( event.campaignDescription() ).campaignStatus( event.campaignStatus() )
                .startDate( event.startDate() ).endDate( event.endDate() ).teamMemberId( event.teamMemberId() )
                .teamId( event.teamId() ).teamName( event.teamName() ).organizationId( event.organizationId() )
                .organizationName( event.organizationName() ).assignmentStatus( event.assignmentStatus() )
                .assignedAt( event.assignedAt() ).completedAt( event.completedAt() ).lastUpdated( Instant.now() ).build();
        influencerCampaignViewRepository.save( influencerCampaignView );
        log.info( "Influencer campaign view record created successfully: {}", event.assignmentId() );
    }

    /**
     * Updates a campaign assignment as completed.
     * 
     * @param event The campaign assignment event
     */
    private void updateCampaignAssignmentCompleted( CampaignAssignmentEvent event )
    {
        if ( !influencerCampaignViewRepository.existsById( event.assignmentId() ) )
        {
            log.warn( "Influencer campaign view record not found: {}", event.assignmentId() );
            return;
        }
        InfluencerCampaignView influencerCampaignView = influencerCampaignViewRepository.findById( event.assignmentId() )
                .orElseThrow( () -> new RuntimeException( "Influencer campaign view record not found" ) );
        influencerCampaignView.setAssignmentStatus( event.assignmentStatus() );
        influencerCampaignView.setCompletedAt( event.completedAt() );
        influencerCampaignView.setLastUpdated( Instant.now() );
        influencerCampaignViewRepository.save( influencerCampaignView );
        log.info( "Influencer campaign view record updated as completed: {}", event.assignmentId() );
    }

    /**
     * Removes a campaign assignment from the influencer campaign view.
     * 
     * @param assignmentId The assignment ID to remove
     */
    private void removeFromInfluencerCampaignView( Long assignmentId )
    {
        if ( !influencerCampaignViewRepository.existsById( assignmentId ) )
        {
            log.warn( "Influencer campaign view record not found: {}", assignmentId );
            return;
        }
        influencerCampaignViewRepository.deleteById( assignmentId );
        log.info( "Influencer campaign view record removed successfully: {}", assignmentId );
    }
}
