package io.zaplink.core.dto.event;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.core.common.constants.MessageConstants;

/**
 * Kafka event for team member addition.
 * Published when a new member is added to a team.
 * 
 * This is an immutable record representing team member addition events.
 * Records provide automatic serialization support and are ideal for event objects.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public record TeamMemberAddedEvent( @JsonProperty("event_type") String eventType,
                                    @JsonProperty("event_id") String eventId,
                                    Instant timestamp,
                                    @JsonProperty("team_member_id") Long teamMemberId,
                                    @JsonProperty("team_id") Long teamId,
                                    @JsonProperty("team_name") String teamName,
                                    @JsonProperty("user_id") Long userId,
                                    String username,
                                    String email,
                                    @JsonProperty("first_name") String firstName,
                                    @JsonProperty("last_name") String lastName,
                                    String role,
                                    String status,
                                    @JsonProperty("organization_id") Long organizationId,
                                    @JsonProperty("organization_name") String organizationName,
                                    @JsonProperty("invited_by") Long invitedBy,
                                    @JsonProperty("invited_at") Instant invitedAt )
{
    /**
     * Event type identifier constant.
     */
    public static final String EVENT_TYPE = "team.member-added";
    /**
     * Compact constructor for initialization and validation.
     * Generates event ID and timestamp if not provided.
     */
    public TeamMemberAddedEvent
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
        // Validate required fields
        if ( teamMemberId == null )
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
        if ( role == null )
        {
            throw new IllegalArgumentException( "Role cannot be null" );
        }
        if ( status == null )
        {
            throw new IllegalArgumentException( "Status cannot be null" );
        }
        if ( organizationId == null )
        {
            throw new IllegalArgumentException( "Organization ID cannot be null" );
        }
    }

    /**
     * Factory method for creating a team member added event.
     * 
     * @param teamMemberId Team member ID
     * @param teamId Team ID
     * @param teamName Team name
     * @param userId User ID
     * @param username Username
     * @param email User email
     * @param firstName User first name
     * @param lastName User last name
     * @param role User role
     * @param status Member status
     * @param organizationId Organization ID
     * @param organizationName Organization name
     * @param invitedBy Who sent the invitation
     * @param invitedWhen When invited
     * @return TeamMemberAddedEvent instance
     */
    public static TeamMemberAddedEvent create( Long teamMemberId,
                                               Long teamId,
                                               String teamName,
                                               Long userId,
                                               String username,
                                               String email,
                                               String firstName,
                                               String lastName,
                                               String role,
                                               String status,
                                               Long organizationId,
                                               String organizationName,
                                               Long invitedBy,
                                               Instant invitedWhen )
    {
        return new TeamMemberAddedEvent( EVENT_TYPE,
                                         null,
                                         null,
                                         teamMemberId,
                                         teamId,
                                         teamName,
                                         userId,
                                         username,
                                         email,
                                         firstName,
                                         lastName,
                                         role,
                                         status,
                                         organizationId,
                                         organizationName,
                                         invitedBy,
                                         invitedWhen );
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

    /**
     * Checks if the member is currently pending.
     * 
     * @return true if status is PENDING
     */
    public boolean isPending()
    {
        return MessageConstants.STATUS_PENDING.equals( status );
    }

    /**
     * Checks if the member is currently active.
     * 
     * @return true if status is ACTIVE
     */
    public boolean isActive()
    {
        return MessageConstants.STATUS_ACTIVE.equals( status );
    }

    /**
     * Gets the full display name of the team member.
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
     * Gets the event summary for logging.
     * 
     * @return Event summary string
     */
    public String getEventSummary()
    {
        return String.format( "Team member %s (%s) added to team %s with role %s", getDisplayName(), email, teamName,
                              role );
    }
}
