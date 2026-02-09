// package io.zaplink.manager.controller;
// import java.util.List;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestHeader;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.Parameter;
// import io.swagger.v3.oas.annotations.media.Content;
// import io.swagger.v3.oas.annotations.media.Schema;
// import io.swagger.v3.oas.annotations.responses.ApiResponse;
// import io.swagger.v3.oas.annotations.responses.ApiResponses;
// import io.swagger.v3.oas.annotations.tags.Tag;
// import io.zaplink.manager.common.constants.ControllerConstants;
// import io.zaplink.manager.common.constants.StatusConstants;
// import io.zaplink.manager.dto.response.TeamMemberResponse;
// import io.zaplink.manager.service.TeamManagementService;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// /**
//  * REST controller for team management operations.
//  * Provides read-optimized endpoints for accessing team member information.
//  * 
//  * @author Zaplink Team
//  * @version 1.0
//  * @since 2026-01-31
//  */
// @Slf4j @Validated @RestController @RequiredArgsConstructor @RequestMapping(ControllerConstants.TEAM_BASE_PATH) @Tag(name = ControllerConstants.TAG_TEAM_MANAGEMENT, description = ControllerConstants.TAG_TEAM_MANAGEMENT_DESC)
// public class TMController
// {
//     private final TeamManagementService teamManagementService;
//     @GetMapping("/{teamId}/members") @Operation(summary = ControllerConstants.TEAM_GET_MEMBERS_SUMMARY, description = ControllerConstants.TEAM_GET_MEMBERS_DESC) @ApiResponses(value =
//     { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_MEMBERS_RETRIEVED, content = @Content(schema = @Schema(implementation = TeamMemberResponse.class))),
//       @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_TEAM_NOT_FOUND) })
//     public ResponseEntity<List<TeamMemberResponse>> getTeamMembers( @Parameter(description = ControllerConstants.PARAM_TEAM_ID) @PathVariable Long teamId )
//     {
//         log.info( "Retrieving team members for team: {}", teamId );
//         List<TeamMemberResponse> members = teamManagementService.getTeamMembers( teamId );
//         return ResponseEntity.ok( members );
//     }
//     @GetMapping("/members") @Operation(summary = ControllerConstants.TEAM_GET_ORG_MEMBERS_SUMMARY, description = ControllerConstants.TEAM_GET_ORG_MEMBERS_DESC) @ApiResponses(value =
//     { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_MEMBERS_RETRIEVED, content = @Content(schema = @Schema(implementation = TeamMemberResponse.class))) })
//     public ResponseEntity<List<TeamMemberResponse>> getOrganizationMembers( @Parameter(description = ControllerConstants.PARAM_ORG_ID) @RequestHeader(ControllerConstants.HEADER_ORG_ID) Long organizationId )
//     {
//         log.info( "Retrieving team members for organization: {}", organizationId );
//         List<TeamMemberResponse> members = teamManagementService.getOrganizationMembers( organizationId );
//         return ResponseEntity.ok( members );
//     }
//     @GetMapping("/members/role/{role}") @Operation(summary = ControllerConstants.TEAM_GET_BY_ROLE_SUMMARY, description = ControllerConstants.TEAM_GET_BY_ROLE_DESC) @ApiResponses(value =
//     { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_MEMBERS_RETRIEVED, content = @Content(schema = @Schema(implementation = TeamMemberResponse.class))) })
//     public ResponseEntity<List<TeamMemberResponse>> getMembersByRole( @Parameter(description = ControllerConstants.PARAM_ORG_ID) @RequestHeader(ControllerConstants.HEADER_ORG_ID) Long organizationId,
//                                                                       @Parameter(description = ControllerConstants.PARAM_ROLE) @PathVariable String role )
//     {
//         log.info( "Retrieving team members for organization: {} with role: {}", organizationId, role );
//         List<TeamMemberResponse> members = teamManagementService.getMembersByRole( organizationId, role );
//         return ResponseEntity.ok( members );
//     }
//     @GetMapping("/influencers") @Operation(summary = ControllerConstants.TEAM_GET_INFLUENCERS_SUMMARY, description = ControllerConstants.TEAM_GET_INFLUENCERS_DESC) @ApiResponses(value =
//     { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_INFLUENCERS_RETRIEVED, content = @Content(schema = @Schema(implementation = TeamMemberResponse.class))) })
//     public ResponseEntity<List<TeamMemberResponse>> getInfluencers( @Parameter(description = ControllerConstants.PARAM_ORG_ID) @RequestHeader(ControllerConstants.HEADER_ORG_ID) Long organizationId )
//     {
//         log.info( "Retrieving influencers for organization: {}", organizationId );
//         List<TeamMemberResponse> influencers = teamManagementService.getInfluencers( organizationId );
//         return ResponseEntity.ok( influencers );
//     }
//     @GetMapping("/statistics") @Operation(summary = ControllerConstants.TEAM_GET_STATISTICS_SUMMARY, description = ControllerConstants.TEAM_GET_STATISTICS_DESC) @ApiResponses(value =
//     { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_STATISTICS_RETRIEVED, content = @Content(schema = @Schema(implementation = TeamManagementService.TeamMemberStatistics.class))) })
//     public ResponseEntity<TeamManagementService.TeamMemberStatistics> getOrganizationStatistics( @Parameter(description = ControllerConstants.PARAM_ORG_ID) @RequestHeader(ControllerConstants.HEADER_ORG_ID) Long organizationId )
//     {
//         log.info( "Retrieving team member statistics for organization: {}", organizationId );
//         TeamManagementService.TeamMemberStatistics statistics = teamManagementService
//                 .getOrganizationStatistics( organizationId );
//         return ResponseEntity.ok( statistics );
//     }
// }
