package io.zaplink.manager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zaplink.manager.dto.response.TeamMemberResponse;
import io.zaplink.manager.service.TeamMemberQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for team member query operations.
 * Provides read-optimized endpoints for accessing team member information.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Slf4j @Validated @RestController @RequiredArgsConstructor @RequestMapping("/teams") @Tag(name = "Team Member Queries", description = "Read-optimized APIs for team member information")
public class TeamMemberQueryController
{
    private final TeamMemberQueryService teamMemberQueryService;
    /**
     * Gets all team members for a team.
     * 
     * @param teamId The team ID
     * @return List of TeamMemberResponse
     */
    @GetMapping("/{teamId}/members") @Operation(summary = "Get team members", description = "Retrieves all team members for a specific team")
    public ResponseEntity<List<TeamMemberResponse>> getTeamMembers( @Parameter(description = "Team ID") @PathVariable Long teamId )
    {
        log.info( "Retrieving team members for team: {}", teamId );
        List<TeamMemberResponse> members = teamMemberQueryService.getTeamMembers( teamId );
        return ResponseEntity.ok( members );
    }

    /**
     * Gets all team members for an organization.
     * 
     * @param organizationId The organization ID (from JWT token)
     * @return List of TeamMemberResponse
     */
    @GetMapping("/members") @Operation(summary = "Get organization members", description = "Retrieves all team members for an organization")
    public ResponseEntity<List<TeamMemberResponse>> getOrganizationMembers( @Parameter(description = "Organization ID") @RequestHeader("X-Org-Id") Long organizationId )
    {
        log.info( "Retrieving team members for organization: {}", organizationId );
        List<TeamMemberResponse> members = teamMemberQueryService.getOrganizationMembers( organizationId );
        return ResponseEntity.ok( members );
    }

    /**
     * Gets all team members with a specific role in an organization.
     * 
     * @param organizationId The organization ID (from JWT token)
     * @param role The role to filter by
     * @return List of TeamMemberResponse
     */
    @GetMapping("/members/role/{role}") @Operation(summary = "Get members by role", description = "Retrieves team members filtered by role")
    public ResponseEntity<List<TeamMemberResponse>> getMembersByRole( @Parameter(description = "Organization ID") @RequestHeader("X-Org-Id") Long organizationId,
                                                                      @Parameter(description = "Role to filter by") @PathVariable String role )
    {
        log.info( "Retrieving team members for organization: {} with role: {}", organizationId, role );
        List<TeamMemberResponse> members = teamMemberQueryService.getMembersByRole( organizationId, role );
        return ResponseEntity.ok( members );
    }

    /**
     * Gets all influencers in an organization.
     * 
     * @param organizationId The organization ID (from JWT token)
     * @return List of TeamMemberResponse for influencers
     */
    @GetMapping("/influencers") @Operation(summary = "Get influencers", description = "Retrieves all influencers in an organization")
    public ResponseEntity<List<TeamMemberResponse>> getInfluencers( @Parameter(description = "Organization ID") @RequestHeader("X-Org-Id") Long organizationId )
    {
        log.info( "Retrieving influencers for organization: {}", organizationId );
        List<TeamMemberResponse> influencers = teamMemberQueryService.getInfluencers( organizationId );
        return ResponseEntity.ok( influencers );
    }

    /**
     * Gets team member statistics for an organization.
     * 
     * @param organizationId The organization ID (from JWT token)
     * @return Team member statistics
     */
    @GetMapping("/statistics") @Operation(summary = "Get team statistics", description = "Retrieves team member statistics for an organization")
    public ResponseEntity<TeamMemberQueryService.TeamMemberStatistics> getOrganizationStatistics( @Parameter(description = "Organization ID") @RequestHeader("X-Org-Id") Long organizationId )
    {
        log.info( "Retrieving team member statistics for organization: {}", organizationId );
        TeamMemberQueryService.TeamMemberStatistics statistics = teamMemberQueryService
                .getOrganizationStatistics( organizationId );
        return ResponseEntity.ok( statistics );
    }
}
