package io.zaplink.manager.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO for workflow status changed events consumed from Kafka.
 * Represents the structure of workflow status change events from Core Service.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowStatusChangedEvent {
    
    /**
     * Event type identifier.
     */
    private String eventType;
    
    /**
     * Unique event ID for tracing.
     */
    private String eventId;
    
    /**
     * Timestamp when the event was created.
     */
    private Instant timestamp;
    
    /**
     * Post ID.
     */
    private Long postId;
    
    /**
     * Post title.
     */
    private String title;
    
    /**
     * Previous status.
     */
    private String previousStatus;
    
    /**
     * New status.
     */
    private String newStatus;
    
    /**
     * Author ID.
     */
    private Long authorId;
    
    /**
     * Author name.
     */
    private String authorName;
    
    /**
     * Author email.
     */
    private String authorEmail;
    
    /**
     * Campaign ID if applicable.
     */
    private Long campaignId;
    
    /**
     * Campaign name if applicable.
     */
    private String campaignName;
    
    /**
     * Team ID.
     */
    private Long teamId;
    
    /**
     * Team name.
     */
    private String teamName;
    
    /**
     * Organization ID.
     */
    private Long organizationId;
    
    /**
     * Organization name.
     */
    private String organizationName;
    
    /**
     * Reviewer ID if applicable.
     */
    private Long reviewerId;
    
    /**
     * Reviewer name if applicable.
     */
    private String reviewerName;
    
    /**
     * Review comments if applicable.
     */
    private String reviewComments;
    
    /**
     * When the status change occurred.
     */
    private Instant changedAt;
}
