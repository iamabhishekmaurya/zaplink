package io.zaplink.manager.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO for team member added events consumed from Kafka.
 * Represents the structure of team member addition events from Core Service.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberAddedEvent {
    
    /**
     * Event type identifier.
     */
    private String eventType;
    
    /**
     * Unique event ID for tracing.
     */
    private String eventId;
    
    /**
     * Timestamp when the event was created.
     */
    private Instant timestamp;
    
    /**
     * Team member ID.
     */
    private Long teamMemberId;
    
    /**
     * Team ID.
     */
    private Long teamId;
    
    /**
     * Team name.
     */
    private String teamName;
    
    /**
     * User ID.
     */
    private Long userId;
    
    /**
     * Username.
     */
    private String username;
    
    /**
     * User email.
     */
    private String email;
    
    /**
     * User first name.
     */
    private String firstName;
    
    /**
     * User last name.
     */
    private String lastName;
    
    /**
     * Role assigned to the member.
     */
    private String role;
    
    /**
     * Status of the member.
     */
    private String status;
    
    /**
     * Organization ID.
     */
    private Long organizationId;
    
    /**
     * Organization name.
     */
    private String organizationName;
    
    /**
     * User ID who invited this member.
     */
    private Long invitedBy;
    
    /**
     * When the member was invited.
     */
    private Instant invitedAt;
}
