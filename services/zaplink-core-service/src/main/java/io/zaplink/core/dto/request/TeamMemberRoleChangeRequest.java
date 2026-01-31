package io.zaplink.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for changing a team member's role.
 * Used to update the role of an existing team member.
 * 
 * This is an immutable record representing a team member role change request.
 * Records provide automatic validation support and are ideal for request DTOs.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public record TeamMemberRoleChangeRequest(
    
    /**
     * User ID of the team member whose role is being changed.
     */
    @NotNull(message = "User ID is required")
    Long userId,
    
    /**
     * New role to assign to the team member.
     * Valid values: ADMIN, EDITOR, APPROVER, VIEWER, INFLUENCER
     */
    @NotBlank(message = "New role is required")
    String newRole,
    
    /**
     * Reason for the role change (optional).
     */
    String reason
) {
    
    /**
     * Compact constructor for validation.
     * Validates role values and business rules.
     */
    public TeamMemberRoleChangeRequest {
        // Validate role is one of the allowed values
        if (newRole != null) {
            boolean validRole = newRole.equals("ADMIN") || newRole.equals("EDITOR") || 
                              newRole.equals("APPROVER") || newRole.equals("VIEWER") || 
                              newRole.equals("INFLUENCER");
            if (!validRole) {
                throw new IllegalArgumentException(
                    "New role must be one of: ADMIN, EDITOR, APPROVER, VIEWER, INFLUENCER"
                );
            }
        }
        
        // Validate user ID
        if (userId != null && userId <= 0) {
            throw new IllegalArgumentException("User ID must be positive");
        }
        
        // Validate reason length
        if (reason != null && reason.length() > 500) {
            throw new IllegalArgumentException("Reason cannot exceed 500 characters");
        }
    }
    
    /**
     * Factory method for creating a role change request without a reason.
     * 
     * @param userId User ID
     * @param newRole New role
     * @return TeamMemberRoleChangeRequest instance
     */
    public static TeamMemberRoleChangeRequest withoutReason(
        Long userId,
        String newRole
    ) {
        return new TeamMemberRoleChangeRequest(userId, newRole, null);
    }
    
    /**
     * Factory method for creating a role change request with a reason.
     * 
     * @param userId User ID
     * @param newRole New role
     * @param reason Reason for change
     * @return TeamMemberRoleChangeRequest instance
     */
    public static TeamMemberRoleChangeRequest withReason(
        Long userId,
        String newRole,
        String reason
    ) {
        return new TeamMemberRoleChangeRequest(userId, newRole, reason);
    }
    
    /**
     * Checks if this is a promotion to admin role.
     * 
     * @return true if new role is ADMIN
     */
    public boolean isAdminPromotion() {
        return "ADMIN".equals(newRole);
    }
    
    /**
     * Checks if this is a demotion to viewer role.
     * 
     * @return true if new role is VIEWER
     */
    public boolean isDemotion() {
        return "VIEWER".equals(newRole);
    }
    
    /**
     * Checks if this is an influencer role assignment.
     * 
     * @return true if new role is INFLUENCER
     */
    public boolean isInfluencerAssignment() {
        return "INFLUENCER".equals(newRole);
    }
    
    /**
     * Checks if this is an approver role assignment.
     * 
     * @return true if new role is APPROVER
     */
    public boolean isApproverAssignment() {
        return "APPROVER".equals(newRole);
    }
    
    /**
     * Checks if this is an editor role assignment.
     * 
     * @return true if new role is EDITOR
     */
    public boolean isEditorAssignment() {
        return "EDITOR".equals(newRole);
    }
    
    /**
     * Checks if the role change has a reason provided.
     * 
     * @return true if reason is not null and not empty
     */
    public boolean hasReason() {
        return reason != null && !reason.trim().isEmpty();
    }
    
    /**
     * Gets the role hierarchy level for comparison.
     * Higher numbers indicate higher privilege levels.
     * 
     * @return Role hierarchy level (0-4)
     */
    public int getRoleHierarchyLevel() {
        return switch (newRole) {
            case "ADMIN" -> 4;
            case "APPROVER" -> 3;
            case "EDITOR" -> 2;
            case "INFLUENCER" -> 1;
            case "VIEWER" -> 0;
            default -> -1;
        };
    }
    
    /**
     * Gets a description of the role change.
     * 
     * @return Human-readable description
     */
    public String getChangeDescription() {
        String description = String.format("Role change to %s", newRole);
        if (hasReason()) {
            description += String.format(" (Reason: %s)", reason);
        }
        return description;
    }
}
