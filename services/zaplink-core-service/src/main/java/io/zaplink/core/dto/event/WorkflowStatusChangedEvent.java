package io.zaplink.core.dto.event;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.core.common.constants.MessageConstants;

/**
 * Kafka event for workflow status changes.
 * Published when a post status changes in the workflow.
 * 
 * This is an immutable record representing workflow status change events.
 * Records provide automatic serialization support and are ideal for event objects.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public record WorkflowStatusChangedEvent( @JsonProperty("event_type") String eventType,
                                          @JsonProperty("event_id") String eventId,
                                          Instant timestamp,
                                          @JsonProperty("post_id") Long postId,
                                          String title,
                                          @JsonProperty("previous_status") String previousStatus,
                                          @JsonProperty("new_status") String newStatus,
                                          @JsonProperty("author_id") Long authorId,
                                          @JsonProperty("author_name") String authorName,
                                          @JsonProperty("author_email") String authorEmail,
                                          @JsonProperty("campaign_id") Long campaignId,
                                          @JsonProperty("campaign_name") String campaignName,
                                          @JsonProperty("team_id") Long teamId,
                                          @JsonProperty("team_name") String teamName,
                                          @JsonProperty("organization_id") Long organizationId,
                                          @JsonProperty("organization_name") String organizationName,
                                          @JsonProperty("reviewer_id") Long reviewerId,
                                          @JsonProperty("reviewer_name") String reviewerName,
                                          @JsonProperty("review_comments") String reviewComments,
                                          @JsonProperty("changed_at") Instant changedAt )
{
    /**
     * Event type identifier constant.
     */
    public static final String EVENT_TYPE = "workflow.status-changed";
    /**
     * Compact constructor for initialization and validation.
     * Generates event ID and timestamp if not provided.
     */
    public WorkflowStatusChangedEvent
    {
        // Set default event type if not provided
        if ( eventType == null )
        {
            eventType = EVENT_TYPE;
        }
        // Generate event ID if not provided
        if ( eventId == null )
        {
            eventId = UUID.randomUUID().toString();
        }
        // Generate timestamp if not provided
        if ( timestamp == null )
        {
            timestamp = Instant.now();
        }
        // Set changedAt to timestamp if not provided
        if ( changedAt == null )
        {
            changedAt = timestamp;
        }
        // Validate required fields
        if ( postId == null )
        {
            throw new IllegalArgumentException( "Post ID cannot be null" );
        }
        if ( newStatus == null )
        {
            throw new IllegalArgumentException( "New status cannot be null" );
        }
        if ( authorId == null )
        {
            throw new IllegalArgumentException( "Author ID cannot be null" );
        }
        if ( organizationId == null )
        {
            throw new IllegalArgumentException( "Organization ID cannot be null" );
        }
    }

    /**
     * Factory method for creating a workflow status change event.
     * 
     * @param postId Post ID
     * @param title Post title
     * @param previousStatus Previous status
     * @param newStatus New status
     * @param authorId Author ID
     * @param authorName Author name
     * @param authorEmail Author email
     * @param organizationId Organization ID
     * @param organizationName Organization name
     * @return WorkflowStatusChangedEvent instance
     */
    public static WorkflowStatusChangedEvent create( Long postId,
                                                     String title,
                                                     String previousStatus,
                                                     String newStatus,
                                                     Long authorId,
                                                     String authorName,
                                                     String authorEmail,
                                                     Long organizationId,
                                                     String organizationName )
    {
        return new WorkflowStatusChangedEvent( EVENT_TYPE,
                                               null,
                                               null,
                                               postId,
                                               title,
                                               previousStatus,
                                               newStatus,
                                               authorId,
                                               authorName,
                                               authorEmail,
                                               null,
                                               null,
                                               null,
                                               null,
                                               organizationId,
                                               organizationName,
                                               null,
                                               null,
                                               null,
                                               null );
    }

    /**
     * Factory method for creating an approval event.
     * 
     * @param postId Post ID
     * @param title Post title
     * @param authorId Author ID
     * @param authorName Author name
     * @param reviewerId Reviewer ID
     * @param reviewerName Reviewer name
     * @param reviewComments Review comments
     * @param organizationId Organization ID
     * @param organizationName Organization name
     * @return WorkflowStatusChangedEvent instance
     */
    public static WorkflowStatusChangedEvent approved( Long postId,
                                                       String title,
                                                       Long authorId,
                                                       String authorName,
                                                       Long reviewerId,
                                                       String reviewerName,
                                                       String reviewComments,
                                                       Long organizationId,
                                                       String organizationName )
    {
        return new WorkflowStatusChangedEvent( EVENT_TYPE,
                                               null,
                                               null,
                                               postId,
                                               title,
                                               MessageConstants.POST_STATUS_SUBMITTED,
                                               MessageConstants.POST_STATUS_APPROVED,
                                               authorId,
                                               authorName,
                                               null,
                                               null,
                                               null,
                                               null,
                                               null,
                                               organizationId,
                                               organizationName,
                                               reviewerId,
                                               reviewerName,
                                               reviewComments,
                                               null );
    }

    /**
     * Factory method for creating a rejection event.
     * 
     * @param postId Post ID
     * @param title Post title
     * @param authorId Author ID
     * @param authorName Author name
     * @param reviewerId Reviewer ID
     * @param reviewerName Reviewer name
     * @param reviewComments Review comments
     * @param organizationId Organization ID
     * @param organizationName Organization name
     * @return WorkflowStatusChangedEvent instance
     */
    public static WorkflowStatusChangedEvent rejected( Long postId,
                                                       String title,
                                                       Long authorId,
                                                       String authorName,
                                                       Long reviewerId,
                                                       String reviewerName,
                                                       String reviewComments,
                                                       Long organizationId,
                                                       String organizationName )
    {
        return new WorkflowStatusChangedEvent( EVENT_TYPE,
                                               null,
                                               null,
                                               postId,
                                               title,
                                               MessageConstants.POST_STATUS_SUBMITTED,
                                               MessageConstants.POST_STATUS_REJECTED,
                                               authorId,
                                               authorName,
                                               null,
                                               null,
                                               null,
                                               null,
                                               null,
                                               organizationId,
                                               organizationName,
                                               reviewerId,
                                               reviewerName,
                                               reviewComments,
                                               null );
    }

    /**
     * Checks if this is an approval event.
     * 
     * @return true if new status is APPROVED
     */
    public boolean isApproval()
    {
        return MessageConstants.POST_STATUS_APPROVED.equals( newStatus );
    }

    /**
     * Checks if this is a rejection event.
     * 
     * @return true if new status is REJECTED
     */
    public boolean isRejection()
    {
        return MessageConstants.POST_STATUS_REJECTED.equals( newStatus );
    }

    /**
     * Checks if this is a submission event.
     * 
     * @return true if new status is SUBMITTED
     */
    public boolean isSubmission()
    {
        return MessageConstants.POST_STATUS_SUBMITTED.equals( newStatus );
    }

    /**
     * Checks if this event has a reviewer.
     * 
     * @return true if reviewer ID is not null
     */
    public boolean hasReviewer()
    {
        return reviewerId != null;
    }

    /**
     * Gets the status change description.
     * 
     * @return Human-readable status change description
     */
    public String getStatusChangeDescription()
    {
        if ( previousStatus == null )
        {
            return "Created with status: " + newStatus;
        }
        return "Status changed from " + previousStatus + " to " + newStatus;
    }
}
