package io.zaplink.core.common.enums;

/**
 * Enum for valid team member statuses within the Zaplink platform.
 * Defines the current state and access level of team members.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public enum TeamMemberStatus {
    
    /**
     * Member is active and has full access to their role-based permissions.
     * This is the normal operating state for team members.
     */
    ACTIVE("Member is active and has full access"),
    
    /**
     * Member is temporarily suspended and cannot access team resources.
     * Used for temporary suspensions while maintaining the team member record.
     */
    INACTIVE("Member is temporarily suspended"),
    
    /**
     * Member has been invited but hasn't joined yet.
     * Waiting for the user to accept the team invitation.
     */
    PENDING("Member has been invited but hasn't joined yet");
    
    private final String description;
    
    TeamMemberStatus(String description) {
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
     * Checks if the member has active access to team resources.
     * 
     * @return true if the status is ACTIVE
     */
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    /**
     * Checks if the member can access team resources.
     * 
     * @return true if the status is ACTIVE (PENDING and INACTIVE cannot access)
     */
    public boolean hasAccess() {
        return this == ACTIVE;
    }
    
    /**
     * Checks if the member is waiting for invitation acceptance.
     * 
     * @return true if the status is PENDING
     */
    public boolean isPending() {
        return this == PENDING;
    }
    
    /**
     * Checks if the member is suspended.
     * 
     * @return true if the status is INACTIVE
     */
    public boolean isSuspended() {
        return this == INACTIVE;
    }
    
    /**
     * Finds a status by its name (case-insensitive).
     * 
     * @param statusName The status name to search for
     * @return The matching TeamMemberStatus, or null if not found
     */
    public static TeamMemberStatus fromName(String statusName) {
        if (statusName == null) {
            return null;
        }
        
        try {
            return TeamMemberStatus.valueOf(statusName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * Gets all statuses that allow access to team resources.
     * 
     * @return Array of statuses with access permissions
     */
    public static TeamMemberStatus[] getAccessibleStatuses() {
        return new TeamMemberStatus[]{ACTIVE};
    }
    
    /**
     * Gets all statuses that indicate the member is not fully active.
     * 
     * @return Array of non-active statuses
     */
    public static TeamMemberStatus[] getNonActiveStatuses() {
        return new TeamMemberStatus[]{PENDING, INACTIVE};
    }
}
