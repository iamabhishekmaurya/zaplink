package io.zaplink.core.service;

import io.zaplink.core.common.constants.LogConstants;
import io.zaplink.core.dto.event.TeamMemberAddedEvent;
import io.zaplink.core.dto.event.WorkflowStatusChangedEvent;
import io.zaplink.core.dto.response.biopage.BioPageResponse;
import io.zaplink.core.dto.response.biolink.BioLinkResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

/**
 * Service for publishing Kafka events for team and workflow changes.
 * Handles event publishing to keep the Manager Service read model synchronized.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Slf4j @Service @RequiredArgsConstructor
public class EventPublisherService
{
    private final KafkaTemplate<String, Object> kafkaTemplate;
    // Topic names
    private static final String                 TEAM_EVENTS_TOPIC     = "team-events";
    private static final String                 WORKFLOW_EVENTS_TOPIC = "workflow-events";
    private static final String                 BIO_EVENTS_TOPIC      = "bio-events";
    /**
     * Publishes a team member added event.
     * 
     * @param event The team member added event to publish
     */
    public void publishTeamMemberAddedEvent( TeamMemberAddedEvent event )
    {
        try
        {
            // Create new event with metadata (records are immutable)
            TeamMemberAddedEvent eventWithMetadata = new TeamMemberAddedEvent( event.eventType(),
                                                                               UUID.randomUUID().toString(),
                                                                               Instant.now(),
                                                                               event.teamMemberId(),
                                                                               event.teamId(),
                                                                               event.teamName(),
                                                                               event.userId(),
                                                                               event.username(),
                                                                               event.email(),
                                                                               event.firstName(),
                                                                               event.lastName(),
                                                                               event.role(),
                                                                               event.status(),
                                                                               event.organizationId(),
                                                                               event.organizationName(),
                                                                               event.invitedBy(),
                                                                               event.invitedAt() );
            log.info( LogConstants.EVENT_PUBLISHING, "Publishing team member added event: {}",
                      eventWithMetadata.eventId() );
            kafkaTemplate.send( TEAM_EVENTS_TOPIC, eventWithMetadata.eventId(), eventWithMetadata )
                    .whenComplete( ( result, ex ) -> {
                        if ( ex != null )
                        {
                            log.error( LogConstants.EVENT_PUBLISH_ERROR, "Failed to publish team member added event",
                                       ex );
                        }
                        else
                        {
                            log.info( LogConstants.EVENT_PUBLISHED,
                                      "Team member added event published successfully: {}",
                                      eventWithMetadata.eventId() );
                        }
                    } );
        }
        catch ( Exception ex )
        {
            log.error( LogConstants.EVENT_PUBLISH_ERROR, "Error publishing team member added event", ex );
            throw new RuntimeException( "Failed to publish team member added event", ex );
        }
    }

    /**
     * Publishes a workflow status changed event.
     * 
     * @param event The workflow status changed event to publish
     */
    public void publishWorkflowStatusChangedEvent( WorkflowStatusChangedEvent event )
    {
        try
        {
            // Create new event with metadata (records are immutable)
            WorkflowStatusChangedEvent eventWithMetadata = new WorkflowStatusChangedEvent( event.eventType(),
                                                                                           UUID.randomUUID().toString(),
                                                                                           Instant.now(),
                                                                                           event.postId(),
                                                                                           event.title(),
                                                                                           event.previousStatus(),
                                                                                           event.newStatus(),
                                                                                           event.authorId(),
                                                                                           event.authorName(),
                                                                                           event.authorEmail(),
                                                                                           event.campaignId(),
                                                                                           event.campaignName(),
                                                                                           event.teamId(),
                                                                                           event.teamName(),
                                                                                           event.organizationId(),
                                                                                           event.organizationName(),
                                                                                           event.reviewerId(),
                                                                                           event.reviewerName(),
                                                                                           event.reviewComments(),
                                                                                           event.changedAt() );
            log.info( LogConstants.EVENT_PUBLISHING, "Publishing workflow status changed event: {}",
                      eventWithMetadata.eventId() );
            kafkaTemplate.send( WORKFLOW_EVENTS_TOPIC, eventWithMetadata.eventId(), eventWithMetadata )
                    .whenComplete( ( result, ex ) -> {
                        if ( ex != null )
                        {
                            log.error( LogConstants.EVENT_PUBLISH_ERROR,
                                       "Failed to publish workflow status changed event", ex );
                        }
                        else
                        {
                            log.info( LogConstants.EVENT_PUBLISHED,
                                      "Workflow status changed event published successfully: {}",
                                      eventWithMetadata.eventId() );
                        }
                    } );
        }
        catch ( Exception ex )
        {
            log.error( LogConstants.EVENT_PUBLISH_ERROR, "Error publishing workflow status changed event", ex );
            throw new RuntimeException( "Failed to publish workflow status changed event", ex );
        }
    }

    /**
     * Publishes a bio page event.
     */
    public void publishBioPageEvent( String eventType, BioPageResponse bioPage )
    {
        try
        {
            io.zaplink.core.dto.event.BioPageEvent event = new io.zaplink.core.dto.event.BioPageEvent( eventType,
                                                                                                       UUID.randomUUID()
                                                                                                               .toString(),
                                                                                                       Instant.now(),
                                                                                                       bioPage );
            log.info( LogConstants.EVENT_PUBLISHING, "Publishing bio page event: {}", event.eventId() );
            kafkaTemplate.send( BIO_EVENTS_TOPIC, event.eventId(), event ).whenComplete( ( result, ex ) -> {
                if ( ex != null )
                {
                    log.error( LogConstants.EVENT_PUBLISH_ERROR, "Failed to publish bio page event", ex );
                }
                else
                {
                    log.info( LogConstants.EVENT_PUBLISHED, "Bio page event published successfully: {}",
                              event.eventId() );
                }
            } );
        }
        catch ( Exception ex )
        {
            log.error( LogConstants.EVENT_PUBLISH_ERROR, "Error publishing bio page event", ex );
        }
    }

    /**
     * Publishes a bio link event.
     */
    public void publishBioLinkEvent( String eventType, BioLinkResponse bioLink, Long pageId )
    {
        try
        {
            io.zaplink.core.dto.event.BioLinkEvent event = new io.zaplink.core.dto.event.BioLinkEvent( eventType,
                                                                                                       UUID.randomUUID()
                                                                                                               .toString(),
                                                                                                       Instant.now(),
                                                                                                       bioLink,
                                                                                                       pageId );
            log.info( LogConstants.EVENT_PUBLISHING, "Publishing bio link event: {}", event.eventId() );
            kafkaTemplate.send( BIO_EVENTS_TOPIC, event.eventId(), event ).whenComplete( ( result, ex ) -> {
                if ( ex != null )
                {
                    log.error( LogConstants.EVENT_PUBLISH_ERROR, "Failed to publish bio link event", ex );
                }
                else
                {
                    log.info( LogConstants.EVENT_PUBLISHED, "Bio link event published successfully: {}",
                              event.eventId() );
                }
            } );
        }
        catch ( Exception ex )
        {
            log.error( LogConstants.EVENT_PUBLISH_ERROR, "Error publishing bio link event", ex );
        }
    }
}
