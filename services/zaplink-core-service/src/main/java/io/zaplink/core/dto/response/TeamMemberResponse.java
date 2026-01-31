package io.zaplink.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO for team member information in responses.
 * Contains user details and their role within the team.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamMemberResponse {
    
    /**
     * Team member ID.
     */
    private Long id;
    
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
     * Role within the team.
     */
    private String role;
    
    /**
     * Status of the team member.
     */
    private String status;
    
    /**
     * When the member was invited.
     */
    private Instant invitedAt;
    
    /**
     * When the member joined the team.
     */
    private Instant joinedAt;
    
    /**
     * Organization ID.
     */
    private Long organizationId;
    
    /**
     * Organization name.
     */
    private String organizationName;
}
