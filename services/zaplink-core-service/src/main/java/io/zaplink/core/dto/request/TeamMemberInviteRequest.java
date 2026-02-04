package io.zaplink.core.dto.request;

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
public record TeamMemberInviteRequest( @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
                                       @NotBlank(message = "Role is required") String role,
                                       @NotNull(message = "Team ID is required") Long teamId,
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
            boolean validRole = role.equals( "ADMIN" ) || role.equals( "EDITOR" ) || role.equals( "APPROVER" )
                    || role.equals( "VIEWER" ) || role.equals( "INFLUENCER" );
            if ( !validRole )
            {
                throw new IllegalArgumentException( "Role must be one of: ADMIN, EDITOR, APPROVER, VIEWER, INFLUENCER" );
            }
        }
        // Validate team ID is positive
        if ( teamId != null && teamId <= 0 )
        {
            throw new IllegalArgumentException( "Team ID must be positive" );
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
        return "ADMIN".equals( role );
    }

    /**
     * Checks if this is an influencer role invitation.
     * 
     * @return true if role is INFLUENCER
     */
    public boolean isInfluencerInvitation()
    {
        return "INFLUENCER".equals( role );
    }
}
