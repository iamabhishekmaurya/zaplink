package io.zaplink.core.common.enums;

/**
 * Enum for valid post statuses within the Zaplink workflow system.
 * Defines the state of content through the approval and publishing process.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public enum PostStatus {
    
    /**
     * Post is being written and is not yet ready for review.
     * Only visible to the author and administrators.
     */
    DRAFT("Post is being written"),
    
    /**
     * Post submitted for approval and waiting for reviewer action.
     * Visible to author, approvers, and administrators.
     */
    SUBMITTED("Post submitted for approval"),
    
    /**
     * Post approved and ready to publish.
     * Can be published immediately or scheduled for later.
     */
    APPROVED("Post approved and ready to publish"),
    
    /**
     * Post rejected by reviewer and needs revisions.
     * Returned to author for changes and resubmission.
     */
    REJECTED("Post rejected by reviewer"),
    
    /**
     * Post has been published and is live.
     * Visible to the intended audience according to campaign settings.
     */
    PUBLISHED("Post has been published");
    
    private final String description;
    
    PostStatus(String description) {
        this.description = description;
    }
    
    /**
     * Gets the description of this status.
     * 
     * @return Human-readable description of the status
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if the post is in a draft state.
     * 
     * @return true if the status is DRAFT
     */
    public boolean isDraft() {
        return this == DRAFT;
    }
    
    /**
     * Checks if the post is pending review.
     * 
     * @return true if the status is SUBMITTED
     */
    public boolean isPendingReview() {
        return this == SUBMITTED;
    }
    
    /**
     * Checks if the post has been approved.
     * 
     * @return true if the status is APPROVED
     */
    public boolean isApproved() {
        return this == APPROVED;
    }
    
    /**
     * Checks if the post has been rejected.
     * 
     * @return true if the status is REJECTED
     */
    public boolean isRejected() {
        return this == REJECTED;
    }
    
    /**
     * Checks if the post is published.
     * 
     * @return true if the status is PUBLISHED
     */
    public boolean isPublished() {
        return this == PUBLISHED;
    }
    
    /**
     * Checks if the post can be edited by the author.
     * 
     * @return true if the post can be edited (DRAFT, REJECTED)
     */
    public boolean isEditable() {
        return this == DRAFT || this == REJECTED;
    }
    
    /**
     * Checks if the post can be submitted for review.
     * 
     * @return true if the post can be submitted (DRAFT, REJECTED)
     */
    public boolean canBeSubmitted() {
        return this == DRAFT || this == REJECTED;
    }
    
    /**
     * Checks if the post can be reviewed.
     * 
     * @return true if the post can be reviewed (SUBMITTED)
     */
    public boolean canBeReviewed() {
        return this == SUBMITTED;
    }
    
    /**
     * Checks if the post can be published.
     * 
     * @return true if the post can be published (APPROVED)
     */
    public boolean canBePublished() {
        return this == APPROVED;
    }
    
    /**
     * Checks if the post is in a final state (no further workflow changes).
     * 
     * @return true if the status is final (PUBLISHED)
     */
    public boolean isFinalState() {
        return this == PUBLISHED;
    }
    
    /**
     * Finds a status by its name (case-insensitive).
     * 
     * @param statusName The status name to search for
     * @return The matching PostStatus, or null if not found
     */
    public static PostStatus fromName(String statusName) {
        if (statusName == null) {
            return null;
        }
        
        try {
            return PostStatus.valueOf(statusName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * Gets all statuses that allow editing by the author.
     * 
     * @return Array of editable statuses
     */
    public static PostStatus[] getEditableStatuses() {
        return new PostStatus[]{DRAFT, REJECTED};
    }
    
    /**
     * Gets all statuses that require reviewer attention.
     * 
     * @return Array of statuses pending review
     */
    public static PostStatus[] getPendingReviewStatuses() {
        return new PostStatus[]{SUBMITTED};
    }
    
    /**
     * Gets all statuses that indicate completion of the workflow.
     * 
     * @return Array of final statuses
     */
    public static PostStatus[] getFinalStatuses() {
        return new PostStatus[]{PUBLISHED};
    }
}
