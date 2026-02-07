package io.zaplink.core.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.common.constants.MessageConstants;
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
public record TeamMemberRoleChangeRequest( @JsonProperty("user_id") @NotNull(message = ErrorConstant.VALIDATION_USER_ID_REQUIRED) Long userId,
                                           @JsonProperty("new_role") @NotBlank(message = ErrorConstant.VALIDATION_NEW_ROLE_REQUIRED) String newRole,
                                           String reason )
{
    /**
     * Compact constructor for validation.
     * Validates role values and business rules.
     */
    public TeamMemberRoleChangeRequest
    {
        // Validate role is one of the allowed values
        if ( newRole != null )
        {
            boolean validRole = newRole.equals( MessageConstants.ROLE_ADMIN )
                    || newRole.equals( MessageConstants.ROLE_EDITOR )
                    || newRole.equals( MessageConstants.ROLE_APPROVER )
                    || newRole.equals( MessageConstants.ROLE_VIEWER )
                    || newRole.equals( MessageConstants.ROLE_INFLUENCER );
            if ( !validRole )
            {
                throw new IllegalArgumentException( ErrorConstant.ERROR_ROLE_NOT_ALLOWED );
            }
        }
        // Validate user ID
        if ( userId != null && userId <= 0 )
        {
            throw new IllegalArgumentException( ErrorConstant.ERROR_USER_ID_MUST_BE_POSITIVE );
        }
        // Validate reason length
        if ( reason != null && reason.length() > 500 )
        {
            throw new IllegalArgumentException( ErrorConstant.ERROR_REASON_LENGTH_TOO_LONG );
        }
    }

    /**
     * Factory method for creating a role change request without a reason.
     * 
     * @param userId User ID
     * @param newRole New role
     * @return TeamMemberRoleChangeRequest instance
     */
    public static TeamMemberRoleChangeRequest withoutReason( Long userId, String newRole )
    {
        return new TeamMemberRoleChangeRequest( userId, newRole, null );
    }

    /**
     * Factory method for creating a role change request with a reason.
     * 
     * @param userId User ID
     * @param newRole New role
     * @param reason Reason for change
     * @return TeamMemberRoleChangeRequest instance
     */
    public static TeamMemberRoleChangeRequest withReason( Long userId, String newRole, String reason )
    {
        return new TeamMemberRoleChangeRequest( userId, newRole, reason );
    }

    /**
     * Checks if this is a promotion to admin role.
     * 
     * @return true if new role is ADMIN
     */
    public boolean isAdminPromotion()
    {
        return MessageConstants.ROLE_ADMIN.equals( newRole );
    }

    /**
     * Checks if this is a demotion to viewer role.
     * 
     * @return true if new role is VIEWER
     */
    public boolean isDemotion()
    {
        return MessageConstants.ROLE_VIEWER.equals( newRole );
    }

    /**
     * Checks if this is an influencer role assignment.
     * 
     * @return true if new role is INFLUENCER
     */
    public boolean isInfluencerAssignment()
    {
        return MessageConstants.ROLE_INFLUENCER.equals( newRole );
    }

    /**
     * Checks if this is an approver role assignment.
     * 
     * @return true if new role is APPROVER
     */
    public boolean isApproverAssignment()
    {
        return MessageConstants.ROLE_APPROVER.equals( newRole );
    }

    /**
     * Checks if this is an editor role assignment.
     * 
     * @return true if new role is EDITOR
     */
    public boolean isEditorAssignment()
    {
        return MessageConstants.ROLE_EDITOR.equals( newRole );
    }

    /**
     * Checks if the role change has a reason provided.
     * 
     * @return true if reason is not null and not empty
     */
    public boolean hasReason()
    {
        return reason != null && !reason.trim().isEmpty();
    }

    /**
     * Gets the role hierarchy level for comparison.
     * Higher numbers indicate higher privilege levels.
     * 
     * @return Role hierarchy level (0-4)
     */
    public int getRoleHierarchyLevel()
    {
        return switch ( newRole )
        {
            case MessageConstants.ROLE_ADMIN -> 4;
            case MessageConstants.ROLE_APPROVER -> 3;
            case MessageConstants.ROLE_EDITOR -> 2;
            case MessageConstants.ROLE_INFLUENCER -> 1;
            case MessageConstants.ROLE_VIEWER -> 0;
            default -> -1;
        };
    }

    /**
     * Gets a description of the role change.
     * 
     * @return Human-readable description
     */
    public String getChangeDescription()
    {
        String description = String.format( "Role change to %s", newRole );
        if ( hasReason() )
        {
            description += String.format( " (Reason: %s)", reason );
        }
        return description;
    }
}
