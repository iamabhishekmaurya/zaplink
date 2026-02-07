package io.zaplink.core.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for team member information in responses.
 * Contains user details and their role within the team.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public record TeamMemberResponse( Long id,
                                  @JsonProperty("team_id") Long teamId,
                                  @JsonProperty("team_name") String teamName,
                                  @JsonProperty("user_id") Long userId,
                                  String username,
                                  String email,
                                  @JsonProperty("first_name") String firstName,
                                  @JsonProperty("last_name") String lastName,
                                  String role,
                                  String status,
                                  @JsonProperty("invited_at") Instant invitedAt,
                                  @JsonProperty("joined_at") Instant joinedAt,
                                  @JsonProperty("organization_id") Long organizationId,
                                  @JsonProperty("organization_name") String organizationName )
{
}
