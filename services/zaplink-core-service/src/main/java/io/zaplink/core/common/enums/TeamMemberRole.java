package io.zaplink.core.common.enums;

/**
 * Enum for valid team member roles within the Zaplink platform.
 * Defines the permission levels and capabilities for team members.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public enum TeamMemberRole {
    
    /**
     * Full administrative access to team resources and member management.
     * Can manage team settings, invite/remove members, and has full content access.
     */
    ADMIN("Full access to team resources and member management"),
    
    /**
     * Can create and edit content, submit for approval.
     * Has access to content creation tools but requires approval for publishing.
     */
    EDITOR("Can create and edit content, submit for approval"),
    
    /**
     * Can approve or reject submitted content.
     * Has authority to review and make decisions on content submissions.
     */
    APPROVER("Can approve or reject submitted content"),
    
    /**
     * Read-only access to team resources.
     * Can view content and team information but cannot make changes.
     */
    VIEWER("Read-only access to team resources"),
    
    /**
     * Limited access to assigned campaigns only.
     * Can work on specific campaigns as assigned by team administrators.
     */
    INFLUENCER("Limited access to assigned campaigns only");
    
    private final String description;
    
    TeamMemberRole(String description) {
        this.description = description;
    }
    
    /**
     * Gets the description of this role.
     * 
     * @return Human-readable description of the role
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if this role has administrative privileges.
     * 
     * @return true if the role is ADMIN
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }
    
    /**
     * Checks if this role can approve content.
     * 
     * @return true if the role can approve (ADMIN or APPROVER)
     */
    public boolean canApprove() {
        return this == ADMIN || this == APPROVER;
    }
    
    /**
     * Checks if this role can create content.
     * 
     * @return true if the role can create content (ADMIN, EDITOR, INFLUENCER)
     */
    public boolean canCreateContent() {
        return this == ADMIN || this == EDITOR || this == INFLUENCER;
    }
    
    /**
     * Checks if this role can edit content.
     * 
     * @return true if the role can edit content (ADMIN, EDITOR)
     */
    public boolean canEditContent() {
        return this == ADMIN || this == EDITOR;
    }
    
    /**
     * Gets the hierarchy level of this role for comparison.
     * Higher numbers indicate higher privilege levels.
     * 
     * @return Role hierarchy level (0-4)
     */
    public int getHierarchyLevel() {
        return switch (this) {
            case ADMIN -> 4;
            case APPROVER -> 3;
            case EDITOR -> 2;
            case INFLUENCER -> 1;
            case VIEWER -> 0;
        };
    }
    
    /**
     * Finds a role by its name (case-insensitive).
     * 
     * @param roleName The role name to search for
     * @return The matching TeamMemberRole, or null if not found
     */
    public static TeamMemberRole fromName(String roleName) {
        if (roleName == null) {
            return null;
        }
        
        try {
            return TeamMemberRole.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * Gets all roles that can approve content.
     * 
     * @return Array of roles with approval privileges
     */
    public static TeamMemberRole[] getApprovalRoles() {
        return new TeamMemberRole[]{ADMIN, APPROVER};
    }
    
    /**
     * Gets all roles that can create content.
     * 
     * @return Array of roles with content creation privileges
     */
    public static TeamMemberRole[] getContentCreationRoles() {
        return new TeamMemberRole[]{ADMIN, EDITOR, INFLUENCER};
    }
}
