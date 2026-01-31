package io.zaplink.manager.dto.response;

import java.time.Instant;

/**
 * DTO for team member information in responses from Manager Service.
 * Contains user details and their role within the team from the read model.
 * 
 * This is an immutable record representing team member data for API responses.
 * Records provide automatic implementation of equals(), hashCode(), toString(),
 * and constructor, making them ideal for DTOs.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public record TeamMemberResponse(
                                  /**
                                   * Team member ID.
                                   */
                                  Long id,
                                  /**
                                   * Team ID.
                                   */
                                  Long teamId,
                                  /**
                                   * Team name.
                                   */
                                  String teamName,
                                  /**
                                   * User ID.
                                   */
                                  Long userId,
                                  /**
                                   * Username.
                                   */
                                  String username,
                                  /**
                                   * User email.
                                   */
                                  String email,
                                  /**
                                   * User first name.
                                   */
                                  String firstName,
                                  /**
                                   * User last name.
                                   */
                                  String lastName,
                                  /**
                                   * Role within the team.
                                   */
                                  String role,
                                  /**
                                   * Status of the team member.
                                   */
                                  String status,
                                  /**
                                   * When the member was invited.
                                   */
                                  Instant invitedAt,
                                  /**
                                   * When the member joined the team.
                                   */
                                  Instant joinedAt,
                                  /**
                                   * Organization ID.
                                   */
                                  Long organizationId,
                                  /**
                                   * Organization name.
                                   */
                                  String organizationName )
{
    /**
     * Compact constructor for validation and null checks.
     * Ensures all required fields are properly initialized.
     */
    public TeamMemberResponse
    {
        if ( id == null )
        {
            throw new IllegalArgumentException( "Team member ID cannot be null" );
        }
        if ( teamId == null )
        {
            throw new IllegalArgumentException( "Team ID cannot be null" );
        }
        if ( userId == null )
        {
            throw new IllegalArgumentException( "User ID cannot be null" );
        }
        if ( organizationId == null )
        {
            throw new IllegalArgumentException( "Organization ID cannot be null" );
        }
        if ( role == null )
        {
            throw new IllegalArgumentException( "Role cannot be null" );
        }
        if ( status == null )
        {
            throw new IllegalArgumentException( "Status cannot be null" );
        }
    }

    /**
     * Factory method for creating a response with minimal required fields.
     * Useful for creating test data or partial responses.
     * 
     * @param id Team member ID
     * @param teamId Team ID
     * @param userId User ID
     * @param role Member role
     * @param status Member status
     * @return TeamMemberResponse instance
     */
    public static TeamMemberResponse of( Long id, Long teamId, Long userId, String role, String status )
    {
        return new TeamMemberResponse( id,
                                       teamId,
                                       null,
                                       userId,
                                       null,
                                       null,
                                       null,
                                       null,
                                       role,
                                       status,
                                       null,
                                       null,
                                       null,
                                       null );
    }

    /**
     * Gets the full display name of the team member.
     * Combines first and last name, falling back to username if names are not available.
     * 
     * @return Display name
     */
    public String getDisplayName()
    {
        if ( firstName != null && lastName != null )
        {
            return firstName + " " + lastName;
        }
        return username != null ? username : "Unknown User";
    }

    /**
     * Checks if the member is currently active.
     * 
     * @return true if member status is ACTIVE
     */
    public boolean isActive()
    {
        return "ACTIVE".equals( status );
    }

    /**
     * Checks if the member has administrative privileges.
     * 
     * @return true if member role is ADMIN
     */
    public boolean isAdmin()
    {
        return "ADMIN".equals( role );
    }
}