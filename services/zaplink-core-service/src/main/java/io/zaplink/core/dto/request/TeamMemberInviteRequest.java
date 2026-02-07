package io.zaplink.core.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.common.constants.MessageConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for inviting a user to join a team.
 * Contains the email of the user to invite and the role they should have.
 * 
 * This is an immutable record representing a team member invitation request.
 * Records provide automatic validation support and are ideal for request DTOs.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public record TeamMemberInviteRequest( @NotBlank(message = ErrorConstant.VALIDATION_EMAIL_REQUIRED) @Email(message = ErrorConstant.VALIDATION_INVALID_EMAIL) String email,
                                       @NotBlank(message = ErrorConstant.VALIDATION_ROLE_REQUIRED) String role,
                                       @JsonProperty("team_id") @NotNull(message = ErrorConstant.VALIDATION_TEAM_ID_REQUIRED) Long teamId,
                                       String message )
{
    /**
     * Compact constructor for additional validation.
     * Validates role enum values and team ID.
     */
    public TeamMemberInviteRequest
    {
        // Validate role is one of the allowed values
        if ( role != null )
        {
            boolean validRole = role.equals( MessageConstants.ROLE_ADMIN )
                    || role.equals( MessageConstants.ROLE_EDITOR ) || role.equals( MessageConstants.ROLE_APPROVER )
                    || role.equals( MessageConstants.ROLE_VIEWER ) || role.equals( MessageConstants.ROLE_INFLUENCER );
            if ( !validRole )
            {
                throw new IllegalArgumentException( ErrorConstant.ERROR_ROLE_NOT_ALLOWED );
            }
        }
        // Validate team ID is positive
        if ( teamId != null && teamId <= 0 )
        {
            throw new IllegalArgumentException( ErrorConstant.ERROR_TEAM_ID_MUST_BE_POSITIVE );
        }
    }

    /**
     * Factory method for creating an invitation without a custom message.
     * 
     * @param email User email
     * @param role User role
     * @param teamId Team ID
     * @return TeamMemberInviteRequest instance
     */
    public static TeamMemberInviteRequest withoutMessage( String email, String role, Long teamId )
    {
        return new TeamMemberInviteRequest( email, role, teamId, null );
    }

    /**
     * Checks if this is an admin role invitation.
     * 
     * @return true if role is ADMIN
     */
    public boolean isAdminInvitation()
    {
        return MessageConstants.ROLE_ADMIN.equals( role );
    }

    /**
     * Checks if this is an influencer role invitation.
     * 
     * @return true if role is INFLUENCER
     */
    public boolean isInfluencerInvitation()
    {
        return MessageConstants.ROLE_INFLUENCER.equals( role );
    }
}
