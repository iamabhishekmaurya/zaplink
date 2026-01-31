package io.zaplink.manager.dto.event;

import java.time.Instant;

/**
 * DTO for workflow status changed events consumed from Kafka.
 * Represents the structure of workflow status change events from Core Service.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public record WorkflowStatusChangedEvent(
    /**
     * Event type identifier.
     */
    String eventType,
    
    /**
     * Unique event ID for tracing.
     */
    String eventId,
    
    /**
     * Timestamp when the event was created.
     */
    Instant timestamp,
    
    /**
     * Post ID.
     */
    Long postId,
    
    /**
     * Post title.
     */
    String title,
    
    /**
     * Previous status.
     */
    String previousStatus,
    
    /**
     * New status.
     */
    String newStatus,
    
    /**
     * Author ID.
     */
    Long authorId,
    
    /**
     * Author name.
     */
    String authorName,
    
    /**
     * Author email.
     */
    String authorEmail,
    
    /**
     * Campaign ID if applicable.
     */
    Long campaignId,
    
    /**
     * Campaign name if applicable.
     */
    String campaignName,
    
    /**
     * Team ID.
     */
    Long teamId,
    
    /**
     * Team name.
     */
    String teamName,
    
    /**
     * Organization ID.
     */
    Long organizationId,
    
    /**
     * Organization name.
     */
    String organizationName,
    
    /**
     * Reviewer ID if applicable.
     */
    Long reviewerId,
    
    /**
     * Reviewer name if applicable.
     */
    String reviewerName,
    
    /**
     * Review comments if applicable.
     */
    String reviewComments,
    
    /**
     * When the status change occurred.
     */
    Instant changedAt
) {}
