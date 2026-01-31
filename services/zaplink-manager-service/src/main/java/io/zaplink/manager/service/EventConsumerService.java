package io.zaplink.manager.service;

import java.time.Instant;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zaplink.manager.dto.event.TeamMemberAddedEvent;
import io.zaplink.manager.dto.event.WorkflowStatusChangedEvent;
import io.zaplink.manager.entity.PendingPostView;
import io.zaplink.manager.entity.TeamMemberView;
import io.zaplink.manager.repository.PendingPostViewRepository;
import io.zaplink.manager.repository.TeamMemberViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for consuming Kafka events from Core Service.
 * Updates the Manager Service read model based on team and workflow events.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Slf4j @Service @RequiredArgsConstructor
public class EventConsumerService
{
    private final TeamMemberViewRepository  teamMemberViewRepository;
    private final PendingPostViewRepository pendingPostViewRepository;
    // Topic names
    private static final String             TEAM_EVENTS_TOPIC     = "team-events";
    private static final String             WORKFLOW_EVENTS_TOPIC = "workflow-events";
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
            log.info( "Processing team member added event: {}", event.getEventId() );
            // Check if record already exists
            if ( teamMemberViewRepository.existsById( event.getTeamMemberId() ) )
            {
                log.warn( "Team member view record already exists: {}", event.getTeamMemberId() );
                return;
            }
            // Create team member view record
            TeamMemberView teamMemberView = TeamMemberView.builder().id( event.getTeamMemberId() )
                    .teamId( event.getTeamId() ).teamName( event.getTeamName() ).userId( event.getUserId() )
                    .username( event.getUsername() ).userEmail( event.getEmail() ).firstName( event.getFirstName() )
                    .lastName( event.getLastName() ).role( event.getRole() ).status( event.getStatus() )
                    .invitedAt( event.getInvitedAt() ).joinedAt( null ) // Will be updated when user accepts invitation
                    .organizationId( event.getOrganizationId() ).organizationName( event.getOrganizationName() )
                    .lastUpdated( Instant.now() ).build();
            teamMemberViewRepository.save( teamMemberView );
            log.info( "Team member view record created successfully: {}", event.getTeamMemberId() );
        }
        catch ( Exception ex )
        {
            log.error( "Error processing team member added event: {}", event.getEventId(), ex );
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
            log.info( "Processing workflow status changed event: {}", event.getEventId() );
            // Handle different status transitions
            switch ( event.getNewStatus() )
            {
                case "SUBMITTED":
                    // Add to pending post view
                    addToPendingPostView( event );
                    break;
                case "APPROVED":
                case "REJECTED":
                    // Remove from pending post view
                    removeFromPendingPostView( event.getPostId() );
                    break;
                default:
                    log.warn( "Unhandled workflow status: {}", event.getNewStatus() );
            }
            log.info( "Workflow status changed event processed successfully: {}", event.getEventId() );
        }
        catch ( Exception ex )
        {
            log.error( "Error processing workflow status changed event: {}", event.getEventId(), ex );
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
        if ( pendingPostViewRepository.existsById( event.getPostId() ) )
        {
            log.warn( "Pending post view record already exists: {}", event.getPostId() );
            return;
        }
        // Create pending post view record
        PendingPostView pendingPostView = PendingPostView.builder().id( event.getPostId() ).title( event.getTitle() )
                .content( null ) // Content not included in event for security
                .authorId( event.getAuthorId() ).authorName( event.getAuthorName() )
                .authorEmail( event.getAuthorEmail() ).campaignId( event.getCampaignId() )
                .campaignName( event.getCampaignName() ).teamId( event.getTeamId() ).teamName( event.getTeamName() )
                .organizationId( event.getOrganizationId() ).organizationName( event.getOrganizationName() )
                .submittedAt( event.getChangedAt() ).status( event.getNewStatus() ).lastUpdated( Instant.now() )
                .build();
        pendingPostViewRepository.save( pendingPostView );
        log.info( "Pending post view record created successfully: {}", event.getPostId() );
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
}
