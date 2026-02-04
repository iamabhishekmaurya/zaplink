package io.zaplink.core.dto.response;

import java.time.Instant;

/**
 * DTO for team member information in responses.
 * Contains user details and their role within the team.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public record TeamMemberResponse( Long id,
                                  Long teamId,
                                  String teamName,
                                  Long userId,
                                  String username,
                                  String email,
                                  String firstName,
                                  String lastName,
                                  String role,
                                  String status,
                                  Instant invitedAt,
                                  Instant joinedAt,
                                  Long organizationId,
                                  String organizationName )
{
}
