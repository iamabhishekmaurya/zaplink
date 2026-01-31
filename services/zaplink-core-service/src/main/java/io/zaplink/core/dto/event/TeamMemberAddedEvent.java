package io.zaplink.core.dto.event;

import java.time.Instant;
import java.util.UUID;

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
public record TeamMemberAddedEvent(
    
    /**
     * Event type identifier.
     */
    String eventType,
    
    /**
     * Unique event ID for tracing.
     */
    String eventId,
    
    /**
     * Timestamp when the event was created.
     */
    Instant timestamp,
    
    /**
     * Team member ID.
     */
    Long teamMemberId,
    
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
     * Member status.
     */
    String status,
    
    /**
     * Organization ID.
     */
    Long organizationId,
    
    /**
     * Organization name.
     */
    String organizationName,
    
    /**
     * User ID of the person who sent the invitation.
     */
    Long invitedBy,
    
    /**
     * When the member was invited.
     */
    Instant invitedAt
) {
    
    /**
     * Event type identifier constant.
     */
    public static final String EVENT_TYPE = "team.member-added";
    
    /**
     * Compact constructor for initialization and validation.
     * Generates event ID and timestamp if not provided.
     */
    public TeamMemberAddedEvent {
        // Set default event type if not provided
        if (eventType == null) {
            eventType = EVENT_TYPE;
        }
        
        // Generate event ID if not provided
        if (eventId == null) {
            eventId = UUID.randomUUID().toString();
        }
        
        // Generate timestamp if not provided
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        
        // Validate required fields
        if (teamMemberId == null) {
            throw new IllegalArgumentException("Team member ID cannot be null");
        }
        if (teamId == null) {
            throw new IllegalArgumentException("Team ID cannot be null");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if (organizationId == null) {
            throw new IllegalArgumentException("Organization ID cannot be null");
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
    public static TeamMemberAddedEvent create(
        Long teamMemberId,
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
        Instant invitedWhen
    ) {
        return new TeamMemberAddedEvent(
            EVENT_TYPE, null, null, teamMemberId, teamId, teamName, userId,
            username, email, firstName, lastName, role, status,
            organizationId, organizationName, invitedBy, invitedWhen
        );
    }
    
    /**
     * Checks if this is an admin role invitation.
     * 
     * @return true if role is ADMIN
     */
    public boolean isAdminInvitation() {
        return "ADMIN".equals(role);
    }
    
    /**
     * Checks if this is an influencer role invitation.
     * 
     * @return true if role is INFLUENCER
     */
    public boolean isInfluencerInvitation() {
        return "INFLUENCER".equals(role);
    }
    
    /**
     * Checks if the member is currently pending.
     * 
     * @return true if status is PENDING
     */
    public boolean isPending() {
        return "PENDING".equals(status);
    }
    
    /**
     * Checks if the member is currently active.
     * 
     * @return true if status is ACTIVE
     */
    public boolean isActive() {
        return "ACTIVE".equals(status);
    }
    
    /**
     * Gets the full display name of the team member.
     * 
     * @return Display name
     */
    public String getDisplayName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return username != null ? username : "Unknown User";
    }
    
    /**
     * Gets the event summary for logging.
     * 
     * @return Event summary string
     */
    public String getEventSummary() {
        return String.format("Team member %s (%s) added to team %s with role %s", 
            getDisplayName(), email, teamName, role);
    }
}
