package io.zaplink.core.common.enums;

/**
 * Enum for valid campaign statuses within the Zaplink platform.
 * Defines the state of marketing campaigns throughout their lifecycle.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public enum CampaignStatus {
    
    /**
     * Campaign is being prepared and configured.
     * Not yet visible to the public or team members.
     */
    DRAFT("Campaign is being prepared"),
    
    /**
     * Campaign is currently running and active.
     * Visible to target audience and accepting content.
     */
    ACTIVE("Campaign is currently running"),
    
    /**
     * Campaign is temporarily paused.
     * Still visible but not actively running or accepting new content.
     */
    PAUSED("Campaign is temporarily paused for this status."),
    
    /**
     * Campaign has finished its run.
     * No longer active but preserved for historical and reporting purposes.
     */
    COMPLETED("Campaign has finished"),
    
    /**
     * Campaign is inactive.
     * Not running and not accepting content.
     */
    INACTIVE("Campaign is inactive"),
    
    /**
     * Campaign is scheduled for future activation.
     * Configured but not yet active.
     */
    SCHEDULED("Campaign is scheduled for future"),
    
    /**
     * Campaign is deleted.
     * Soft deleted - not visible but preserved for audit purposes.
     */
    DELETED("Campaign is deleted");
    
    private final String description;
    
    CampaignStatus(String description) {
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
     * Checks if the campaign is in draft state.
     * 
     * @return true if the status is DRAFT
     */
    public boolean isDraft() {
        return this == DRAFT;
    }
    
    /**
     * Checks if the campaign is currently active.
     * 
     * @return true if the status is ACTIVE
     */
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    /**
     * Checks if the campaign is paused.
     * 
     * @return true if the status is PAUSED
     */
    public boolean isPaused() {
        return this == PAUSED;
    }
    
    /**
     * Checks if the campaign is completed.
     * 
     * @return true if the status is COMPLETED
     */
    public boolean isCompleted() {
        return this == COMPLETED;
    }
    
    /**
     * Checks if the campaign is inactive.
     * 
     * @return true if the status is INACTIVE
     */
    public boolean isInactive() {
        return this == INACTIVE;
    }
    
    /**
     * Checks if the campaign is scheduled.
     * 
     * @return true if the status is SCHEDULED
     */
    public boolean isScheduled() {
        return this == SCHEDULED;
    }
    
    /**
     * Checks if the campaign is deleted.
     * 
     * @return true if the status is DELETED
     */
    public boolean isDeleted() {
        return this == DELETED;
    }
    
    /**
     * Checks if the campaign can be edited.
     * 
     * @return true if the campaign can be edited (DRAFT, PAUSED, SCHEDULED)
     */
    public boolean isEditable() {
        return this == DRAFT || this == PAUSED || this == SCHEDULED;
    }
    
    /**
     * Checks if the campaign can be activated.
     * 
     * @return true if the campaign can be activated (DRAFT, PAUSED, SCHEDULED)
     */
    public boolean canActivate() {
        return this == DRAFT || this == PAUSED || this == SCHEDULED;
    }
    
    /**
     * Checks if the campaign can be paused.
     * 
     * @return true if the campaign can be paused (ACTIVE)
     */
    public boolean canPause() {
        return this == ACTIVE;
    }
    
    /**
     * Checks if the campaign can be completed.
     * 
     * @return true if the campaign can be completed (ACTIVE, PAUSED)
     */
    public boolean canComplete() {
        return this == ACTIVE || this == PAUSED;
    }
    
    /**
     * Checks if the campaign is in a running state.
     * 
     * @return true if the campaign is running (ACTIVE)
     */
    public boolean isRunning() {
        return this == ACTIVE;
    }
    
    /**
     * Checks if the campaign is in a final state.
     * 
     * @return true if the status is final (COMPLETED, INACTIVE, DELETED)
     */
    public boolean isFinalState() {
        return this == COMPLETED || this == INACTIVE || this == DELETED;
    }
    
    /**
     * Finds a status by its name (case-insensitive).
     * 
     * @param statusName The status name to search for
     * @return The matching CampaignStatus, or null if not found
     */
    public static CampaignStatus fromName(String statusName) {
        if (statusName == null) {
            return null;
        }
        
        try {
            return CampaignStatus.valueOf(statusName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * Gets all statuses that allow editing.
     * 
     * @return Array of editable statuses
     */
    public static CampaignStatus[] getEditableStatuses() {
        return new CampaignStatus[]{DRAFT, PAUSED, SCHEDULED};
    }
    
    /**
     * Gets all statuses that indicate the campaign is running.
     * 
     * @return Array of running statuses
     */
    public static CampaignStatus[] getRunningStatuses() {
        return new CampaignStatus[]{ACTIVE};
    }
    
    /**
     * Gets all statuses that indicate the campaign is not running.
     * 
     * @return Array of non-running statuses
     */
    public static CampaignStatus[] getNonRunningStatuses() {
        return new CampaignStatus[]{DRAFT, PAUSED, COMPLETED, INACTIVE, SCHEDULED, DELETED};
    }
    
    /**
     * Gets all statuses that are considered final.
     * 
     * @return Array of final statuses
     */
    public static CampaignStatus[] getFinalStatuses() {
        return new CampaignStatus[]{COMPLETED, INACTIVE, DELETED};
    }
    
    /**
     * Gets all active statuses (not deleted).
     * 
     * @return Array of active statuses
     */
    public static CampaignStatus[] getActiveStatuses() {
        return new CampaignStatus[]{DRAFT, ACTIVE, PAUSED, COMPLETED, INACTIVE, SCHEDULED};
    }
}
