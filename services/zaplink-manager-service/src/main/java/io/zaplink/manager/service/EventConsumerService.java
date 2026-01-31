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
}
