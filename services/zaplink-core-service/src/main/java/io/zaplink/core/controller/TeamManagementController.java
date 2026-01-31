package io.zaplink.core.controller;

import io.zaplink.core.dto.request.TeamMemberInviteRequest;
import io.zaplink.core.dto.request.TeamMemberRoleChangeRequest;
import io.zaplink.core.dto.response.TeamMemberResponse;
import io.zaplink.core.service.TeamManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST controller for team management operations.
 * Provides endpoints for inviting team members, changing roles, and member management.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Slf4j
@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Validated
@Tag(name = "Team Management", description = "APIs for managing team members and roles")
public class TeamManagementController {
    
    private final TeamManagementService teamManagementService;
    
    /**
     * Invites a user to join a team with a specific role.
     * 
     * @param request The team member invitation request
     * @param invitedBy User ID of the person sending the invitation (from JWT token)
     * @return TeamMemberResponse with the created team member information
     */
    @PostMapping("/invite")
    @Operation(summary = "Invite team member", description = "Invites a user to join a team with a specific role")
    public ResponseEntity<TeamMemberResponse> inviteTeamMember(
            @Valid @RequestBody TeamMemberInviteRequest request,
            @RequestHeader("X-User-Id") Long invitedBy) {
        
        log.info("Inviting team member: {} by user: {}", request.email(), invitedBy);
        
        TeamMemberResponse response = teamManagementService.inviteTeamMember(request, invitedBy);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
    
    /**
     * Changes the role of a team member.
     * 
     * @param request The role change request
     * @return Updated TeamMemberResponse
     */
    @PutMapping("/members/{userId}/role")
    @Operation(summary = "Change team member role", description = "Changes the role of an existing team member")
    public ResponseEntity<TeamMemberResponse> changeTeamMemberRole(
            @Parameter(description = "User ID of the team member") @PathVariable Long userId,
            @Valid @RequestBody TeamMemberRoleChangeRequest request) {
        
        log.info("Changing role for user: {} to: {}", userId, request.newRole());
        
        // Create new request with userId from path variable (records are immutable)
        TeamMemberRoleChangeRequest roleChangeRequest = new TeamMemberRoleChangeRequest(
            userId, 
            request.newRole(), 
            request.reason()
        );
        
        TeamMemberResponse response = teamManagementService.changeTeamMemberRole(roleChangeRequest);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Removes a team member from a team.
     * 
     * @param userId The user ID to remove
     * @param teamId The team ID to remove from
     * @return No content response
     */
    @DeleteMapping("/members/{userId}")
    @Operation(summary = "Remove team member", description = "Removes a team member from a team")
    public ResponseEntity<Void> removeTeamMember(
            @Parameter(description = "User ID of the team member to remove") @PathVariable Long userId,
            @Parameter(description = "Team ID to remove from") @RequestParam Long teamId) {
        
        log.info("Removing team member: {} from team: {}", userId, teamId);
        
        teamManagementService.removeTeamMember(userId, teamId);
        
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Gets all team members for a team.
     * 
     * @param teamId The team ID
     * @return List of TeamMemberResponse
     */
    @GetMapping("/{teamId}/members")
    @Operation(summary = "Get team members", description = "Retrieves all team members for a specific team")
    public ResponseEntity<List<TeamMemberResponse>> getTeamMembers(
            @Parameter(description = "Team ID") @PathVariable Long teamId) {
        
        log.info("Retrieving team members for team: {}", teamId);
        
        List<TeamMemberResponse> members = teamManagementService.getTeamMembers(teamId);
        
        return ResponseEntity.ok(members);
    }
}
