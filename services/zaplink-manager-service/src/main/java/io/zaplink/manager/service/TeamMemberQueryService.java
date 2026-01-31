package io.zaplink.manager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.zaplink.manager.dto.response.TeamMemberResponse;
import io.zaplink.manager.entity.TeamMemberView;
import io.zaplink.manager.repository.TeamMemberViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for querying team member information from the read model.
 * Provides optimized read operations for team management data.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Slf4j @Service @RequiredArgsConstructor
public class TeamMemberQueryService
{
    private final TeamMemberViewRepository teamMemberViewRepository;
    /**
     * Gets all team members for a team.
     * 
     * @param teamId The team ID
     * @return List of TeamMemberResponse
     */
    public List<TeamMemberResponse> getTeamMembers( Long teamId )
    {
        log.info( "Retrieving team members for team: {}", teamId );
        List<TeamMemberView> members = teamMemberViewRepository.findByTeamId( teamId );
        return members.stream().map( this::mapToTeamMemberResponse ).collect( Collectors.toList() );
    }

    /**
     * Gets all team members for an organization.
     * 
     * @param organizationId The organization ID
     * @return List of TeamMemberResponse
     */
    public List<TeamMemberResponse> getOrganizationMembers( Long organizationId )
    {
        log.info( "Retrieving team members for organization: {}", organizationId );
        List<TeamMemberView> members = teamMemberViewRepository.findByOrganizationId( organizationId );
        return members.stream().map( this::mapToTeamMemberResponse ).collect( Collectors.toList() );
    }

    /**
     * Gets all team members with a specific role in an organization.
     * 
     * @param organizationId The organization ID
     * @param role The role to filter by
     * @return List of TeamMemberResponse
     */
    public List<TeamMemberResponse> getMembersByRole( Long organizationId, String role )
    {
        log.info( "Retrieving team members for organization: {} with role: {}", organizationId, role );
        List<TeamMemberView> members = teamMemberViewRepository.findByOrganizationIdAndRole( organizationId, role );
        return members.stream().map( this::mapToTeamMemberResponse ).collect( Collectors.toList() );
    }

    /**
     * Gets all influencers in an organization.
     * 
     * @param organizationId The organization ID
     * @return List of TeamMemberResponse for influencers
     */
    public List<TeamMemberResponse> getInfluencers( Long organizationId )
    {
        log.info( "Retrieving influencers for organization: {}", organizationId );
        List<TeamMemberView> influencers = teamMemberViewRepository.findInfluencersByOrganization( organizationId );
        return influencers.stream().map( this::mapToTeamMemberResponse ).collect( Collectors.toList() );
    }

    /**
     * Gets team member statistics for an organization.
     * 
     * @param organizationId The organization ID
     * @return Team member statistics
     */
    public TeamMemberStatistics getOrganizationStatistics( Long organizationId )
    {
        log.info( "Retrieving team member statistics for organization: {}", organizationId );
        long totalMembers = teamMemberViewRepository.countByOrganizationId( organizationId );
        long adminCount = teamMemberViewRepository.countByOrganizationIdAndRole( organizationId, "ADMIN" );
        long editorCount = teamMemberViewRepository.countByOrganizationIdAndRole( organizationId, "EDITOR" );
        long approverCount = teamMemberViewRepository.countByOrganizationIdAndRole( organizationId, "APPROVER" );
        long viewerCount = teamMemberViewRepository.countByOrganizationIdAndRole( organizationId, "VIEWER" );
        long influencerCount = teamMemberViewRepository.countByOrganizationIdAndRole( organizationId, "INFLUENCER" );
        return TeamMemberStatistics.builder().totalMembers( totalMembers ).adminCount( adminCount )
                .editorCount( editorCount ).approverCount( approverCount ).viewerCount( viewerCount )
                .influencerCount( influencerCount ).build();
    }

    /**
     * Maps TeamMemberView entity to TeamMemberResponse DTO.
     * 
     * @param member The team member view entity
     * @return TeamMemberResponse DTO
     */
    private TeamMemberResponse mapToTeamMemberResponse( TeamMemberView member )
    {
        return new TeamMemberResponse( member.getId(),
                                       member.getTeamId(),
                                       member.getTeamName(),
                                       member.getUserId(),
                                       member.getUsername(),
                                       member.getUserEmail(),
                                       member.getFirstName(),
                                       member.getLastName(),
                                       member.getRole(),
                                       member.getStatus(),
                                       member.getInvitedAt(),
                                       member.getJoinedAt(),
                                       member.getOrganizationId(),
                                       member.getOrganizationName() );
    }
    /**
     * DTO for team member statistics.
     */
    @lombok.Data @lombok.Builder @lombok.AllArgsConstructor @lombok.NoArgsConstructor
    public static class TeamMemberStatistics
    {
        private long totalMembers;
        private long adminCount;
        private long editorCount;
        private long approverCount;
        private long viewerCount;
        private long influencerCount;
    }
}
