package io.zaplink.core.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zaplink.core.common.constants.LogConstants;
import io.zaplink.core.common.enums.TeamMemberRole;
import io.zaplink.core.common.enums.TeamMemberStatus;
import io.zaplink.core.dto.event.TeamMemberAddedEvent;
import io.zaplink.core.dto.request.TeamMemberInviteRequest;
import io.zaplink.core.dto.request.TeamMemberRoleChangeRequest;
import io.zaplink.core.dto.response.TeamMemberResponse;
import io.zaplink.core.entity.Team;
import io.zaplink.core.entity.TeamMember;
import io.zaplink.core.repository.TeamMemberRepository;
import io.zaplink.core.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for team management operations.
 * Handles team member invitations, role changes, and removals.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Slf4j @Service @RequiredArgsConstructor
public class TeamManagementService
{
        private final TeamMemberRepository  teamMemberRepository;
        private final TeamRepository        teamRepository;
        private final EventPublisherService eventPublisherService;
        /**
         * Invites a user to join a team with a specific role.
         * 
         * @param request The team member invitation request
         * @param invitedBy User ID of the person sending the invitation
         * @return TeamMemberResponse with the created team member information
         */
        @Transactional
        public TeamMemberResponse inviteTeamMember( TeamMemberInviteRequest request, Long invitedBy )
        {
                log.info( LogConstants.TEAM_MEMBER_INVITING, "Email: {}, Role: {}, Team: {}", request.email(),
                          request.role(), request.teamId() );
                // Validate team exists
                Team team = teamRepository.findById( request.teamId() )
                                .orElseThrow( () -> new RuntimeException( "Team not found: " + request.teamId() ) );
                // Check if user is already a member
                // Note: In a real implementation, you would look up the user by email
                // For now, we'll create a placeholder user ID
                Long userId = generateUserIdFromEmail( request.email() );
                teamMemberRepository.findByTeamIdAndUserId( request.teamId(), userId ).ifPresent( existing -> {
                        throw new RuntimeException( "User is already a member of this team" );
                } );
                // Create team member
                TeamMember teamMember = TeamMember.builder().teamId( team.getId() ).userId( userId )
                                .role( TeamMemberRole.valueOf( request.role() ) ).status( TeamMemberStatus.PENDING )
                                .invitedBy( invitedBy ).invitedAt( Instant.now() ).joinedAt( null ) // Will be set when user accepts invitation
                                .build();
                teamMember = teamMemberRepository.save( teamMember );
                // Publish event
                TeamMemberAddedEvent event = TeamMemberAddedEvent
                                .create( teamMember.getId(), team.getId(), team.getName(), userId,
                                         generateUsernameFromEmail( request.email() ), request.email(), "", "",
                                         request.role(), teamMember.getStatus().name(), team.getOrganizationId(), "",
                                         invitedBy, teamMember.getInvitedAt() );
                eventPublisherService.publishTeamMemberAddedEvent( event );
                log.info( LogConstants.TEAM_MEMBER_INVITED, "Team member invited successfully: {}",
                          teamMember.getId() );
                return mapToTeamMemberResponse( teamMember, team );
        }

        /**
         * Changes the role of a team member.
         * 
         * @param request The role change request
         * @return Updated TeamMemberResponse
         */
        @Transactional
        public TeamMemberResponse changeTeamMemberRole( TeamMemberRoleChangeRequest request )
        {
                log.info( LogConstants.TEAM_MEMBER_ROLE_CHANGING, "User: {}, New Role: {}", request.userId(),
                          request.newRole() );
                TeamMember teamMember = teamMemberRepository.findByUserId( request.userId() ).stream().findFirst()
                                .orElseThrow( () -> new RuntimeException( "Team member not found: "
                                                + request.userId() ) );
                TeamMemberRole previousRole = teamMember.getRole();
                teamMember.setRole( TeamMemberRole.valueOf( request.newRole() ) );
                TeamMember savedTeamMember = teamMemberRepository.save( teamMember );
                // Note: In a real implementation, you might want to publish a role change event
                log.info( LogConstants.TEAM_MEMBER_ROLE_CHANGED, "Team member role changed successfully: {}",
                          savedTeamMember.getId() );
                Team team = teamRepository.findById( savedTeamMember.getTeamId() )
                                .orElseThrow( () -> new RuntimeException( "Team not found: "
                                                + savedTeamMember.getTeamId() ) );
                return mapToTeamMemberResponse( savedTeamMember, team );
        }

        /**
         * Removes a team member from a team.
         * 
         * @param userId The user ID to remove
         * @param teamId The team ID to remove from
         */
        @Transactional
        public void removeTeamMember( Long userId, Long teamId )
        {
                log.info( LogConstants.TEAM_MEMBER_REMOVING, "User: {}, Team: {}", userId, teamId );
                TeamMember teamMember = teamMemberRepository.findByTeamIdAndUserId( teamId, userId )
                                .orElseThrow( () -> new RuntimeException( "Team member not found" ) );
                teamMemberRepository.delete( teamMember );
                // Note: In a real implementation, you might want to publish a member removed event
                log.info( LogConstants.TEAM_MEMBER_REMOVED, "Team member removed successfully: {}",
                          teamMember.getId() );
        }

        /**
         * Gets all team members for a team.
         * 
         * @param teamId The team ID
         * @return List of TeamMemberResponse
         */
        public List<TeamMemberResponse> getTeamMembers( Long teamId )
        {
                List<TeamMember> members = teamMemberRepository.findByTeamId( teamId );
                Team team = teamRepository.findById( teamId )
                                .orElseThrow( () -> new RuntimeException( "Team not found: " + teamId ) );
                return members.stream().map( member -> mapToTeamMemberResponse( member, team ) )
                                .collect( Collectors.toList() );
        }

        /**
         * Maps TeamMember entity to TeamMemberResponse DTO.
         * 
         * @param teamMember The team member entity
         * @param team The team entity
         * @return TeamMemberResponse DTO
         */
        private TeamMemberResponse mapToTeamMemberResponse( TeamMember teamMember, Team team )
        {
                return new TeamMemberResponse( teamMember.getId(),
                                               teamMember.getTeamId(),
                                               team.getName(),
                                               teamMember.getUserId(),
                                               generateUsernameFromEmail( "" ), // Would be populated from user service
                                               "", // Would be populated from user service
                                               "", // Would be populated from user service
                                               "", // Would be populated from user service
                                               teamMember.getRole().name(),
                                               teamMember.getStatus().name(),
                                               teamMember.getInvitedAt(),
                                               teamMember.getJoinedAt(),
                                               team.getOrganizationId(),
                                               "" // Would be populated from organization service
                );
        }

        /**
         * Generates a placeholder user ID from email.
         * In a real implementation, this would look up the user in the user service.
         * 
         * @param email The email address
         * @return Generated user ID
         */
        private Long generateUserIdFromEmail( String email )
        {
                // Placeholder implementation - in reality, you'd call the auth service
                return (long) Math.abs( email.hashCode() ) % 1000000;
        }

        /**
         * Generates a placeholder username from email.
         * In a real implementation, this would look up the user in the user service.
         * 
         * @param email The email address
         * @return Generated username
         */
        private String generateUsernameFromEmail( String email )
        {
                // Placeholder implementation - in reality, you'd call the auth service
                return email.split( "@" )[0];
        }
}
