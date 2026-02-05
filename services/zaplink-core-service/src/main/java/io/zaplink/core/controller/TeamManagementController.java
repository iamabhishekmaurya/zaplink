package io.zaplink.core.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zaplink.core.common.constants.ControllerConstants;
import io.zaplink.core.common.constants.LogConstants;
import io.zaplink.core.common.constants.StatusConstants;
import io.zaplink.core.dto.request.TeamMemberInviteRequest;
import io.zaplink.core.dto.request.TeamMemberRoleChangeRequest;
import io.zaplink.core.dto.response.TeamMemberResponse;
import io.zaplink.core.service.TeamManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for team management operations.
 * Provides endpoints for inviting team members, changing roles, and member management.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Slf4j @RestController @RequestMapping(ControllerConstants.TEAM_BASE_PATH) @RequiredArgsConstructor @Validated @Tag(name = ControllerConstants.TAG_TEAM_MANAGEMENT, description = ControllerConstants.TAG_TEAM_MANAGEMENT_DESC)
public class TeamManagementController
{
    private final TeamManagementService teamManagementService;
    /**
     * Invites a user to join a team with a specific role.
     * 
     * @param request The team member invitation request
     * @param invitedBy User ID of the person sending the invitation (from JWT token)
     * @return TeamMemberResponse with the created team member information
     */
    @PostMapping(ControllerConstants.TEAM_INVITE_PATH) @Operation(summary = ControllerConstants.TEAM_INVITE_MEMBER_SUMMARY, description = ControllerConstants.TEAM_INVITE_MEMBER_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_201_CREATED, description = ControllerConstants.RESPONSE_201_TEAM_MEMBER_INVITED, content = @Content(schema = @Schema(implementation = TeamMemberResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_INVITATION_DATA),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_TEAM_NOT_FOUND),
      @ApiResponse(responseCode = StatusConstants.STATUS_409_CONFLICT, description = ControllerConstants.RESPONSE_409_USER_ALREADY_MEMBER) })
    public ResponseEntity<TeamMemberResponse> inviteTeamMember( @Valid @RequestBody TeamMemberInviteRequest request,
                                                                @RequestHeader(ControllerConstants.HEADER_USER_ID) Long invitedBy )
    {
        log.info( LogConstants.TEAM_MEMBER_INVITING_WITH_USER, request.email(), invitedBy );
        TeamMemberResponse response = teamManagementService.inviteTeamMember( request, invitedBy );
        return ResponseEntity.status( HttpStatus.CREATED ).body( response );
    }

    /**
     * Changes the role of a team member.
     * 
     * @param request The role change request
     * @return Updated TeamMemberResponse
     */
    @PutMapping(ControllerConstants.TEAM_MEMBER_ROLE_PATH) @Operation(summary = ControllerConstants.TEAM_CHANGE_ROLE_SUMMARY, description = ControllerConstants.TEAM_CHANGE_ROLE_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_TEAM_ROLE_CHANGED, content = @Content(schema = @Schema(implementation = TeamMemberResponse.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_ROLE_DATA),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_TEAM_MEMBER_NOT_FOUND) })
    public ResponseEntity<TeamMemberResponse> changeTeamMemberRole( @Parameter(description = ControllerConstants.PARAM_DESC_USER_ID) @PathVariable Long userId,
                                                                    @Valid @RequestBody TeamMemberRoleChangeRequest request )
    {
        log.info( LogConstants.TEAM_MEMBER_ROLE_CHANGING_WITH_USER, userId, request.newRole() );
        // Create new request with userId from path variable (records are immutable)
        TeamMemberRoleChangeRequest roleChangeRequest = new TeamMemberRoleChangeRequest( userId,
                                                                                         request.newRole(),
                                                                                         request.reason() );
        TeamMemberResponse response = teamManagementService.changeTeamMemberRole( roleChangeRequest );
        return ResponseEntity.ok( response );
    }

    /**
     * Removes a team member from a team.
     * 
     * @param userId The user ID to remove
     * @param teamId The team ID to remove from
     * @return No content response
     */
    @DeleteMapping(ControllerConstants.TEAM_MEMBER_REMOVE_PATH) @Operation(summary = ControllerConstants.TEAM_REMOVE_MEMBER_SUMMARY, description = ControllerConstants.TEAM_REMOVE_MEMBER_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_204_NO_CONTENT, description = ControllerConstants.RESPONSE_204_TEAM_MEMBER_REMOVED),
      @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_TEAM_DATA),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_TEAM_MEMBER_NOT_FOUND) })
    public ResponseEntity<Void> removeTeamMember( @Parameter(description = ControllerConstants.PARAM_DESC_USER_ID_REMOVE) @PathVariable Long userId,
                                                  @Parameter(description = ControllerConstants.PARAM_DESC_TEAM_ID_REMOVE) @RequestParam(ControllerConstants.PARAM_TEAM_ID) Long teamId )
    {
        log.info( LogConstants.TEAM_MEMBER_REMOVING_FROM_TEAM, userId, teamId );
        teamManagementService.removeTeamMember( userId, teamId );
        return ResponseEntity.noContent().build();
    }

    /**
     * Gets all team members for a team.
     * 
     * @param teamId The team ID
     * @return List of TeamMemberResponse
     */
    @GetMapping(ControllerConstants.TEAM_MEMBERS_PATH) @Operation(summary = ControllerConstants.TEAM_GET_MEMBERS_SUMMARY, description = ControllerConstants.TEAM_GET_MEMBERS_DESC) @ApiResponses(value =
    { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_TEAM_MEMBERS_RETRIEVED, content = @Content(schema = @Schema(implementation = List.class))),
      @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_TEAM_NOT_FOUND) })
    public ResponseEntity<List<TeamMemberResponse>> getTeamMembers( @Parameter(description = ControllerConstants.PARAM_DESC_TEAM_ID) @PathVariable Long teamId )
    {
        log.info( LogConstants.TEAM_MEMBERS_RETRIEVING_FOR_TEAM, teamId );
        List<TeamMemberResponse> members = teamManagementService.getTeamMembers( teamId );
        return ResponseEntity.ok( members );
    }
}
