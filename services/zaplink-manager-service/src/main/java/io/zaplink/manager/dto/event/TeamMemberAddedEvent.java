package io.zaplink.manager.dto.event;

import java.time.Instant;

/**
 * DTO for team member added events consumed from Kafka.
 * Represents the structure of team member addition events from Core Service.
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
     * Role assigned to the member.
     */
    String role,
    
    /**
     * Status of the member.
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
     * User ID who invited this member.
     */
    Long invitedBy,
    
    /**
     * When the member was invited.
     */
    Instant invitedAt
) {}
