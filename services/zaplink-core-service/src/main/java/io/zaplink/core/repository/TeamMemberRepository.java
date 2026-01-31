package io.zaplink.core.repository;

import io.zaplink.core.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for TeamMember entity operations.
 * Provides database access methods for team member management.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    
    /**
     * Find team member by team ID and user ID.
     * 
     * @param teamId The team ID
     * @param userId The user ID
     * @return Optional containing the team member if found
     */
    Optional<TeamMember> findByTeamIdAndUserId(Long teamId, Long userId);
    
    /**
     * Find all team members by team ID.
     * 
     * @param teamId The team ID
     * @return List of team members in the team
     */
    List<TeamMember> findByTeamId(Long teamId);
    
    /**
     * Find all team members by user ID.
     * 
     * @param userId The user ID
     * @return List of teams the user belongs to
     */
    List<TeamMember> findByUserId(Long userId);
    
    /**
     * Find all team members by role.
     * 
     * @param role The role to search for
     * @return List of team members with the specified role
     */
    List<TeamMember> findByRole(String role);
    
    /**
     * Find all team members by status.
     * 
     * @param status The status to search for
     * @return List of team members with the specified status
     */
    List<TeamMember> findByStatus(String status);
    
    /**
     * Find all team members by team ID and role.
     * 
     * @param teamId The team ID
     * @param role The role to search for
     * @return List of team members in the team with the specified role
     */
    List<TeamMember> findByTeamIdAndRole(Long teamId, String role);
    
    /**
     * Find all team members by team ID and status.
     * 
     * @param teamId The team ID
     * @param status The status to search for
     * @return List of team members in the team with the specified status
     */
    List<TeamMember> findByTeamIdAndStatus(Long teamId, String status);
    
    /**
     * Count team members by team ID.
     * 
     * @param teamId The team ID
     * @return Number of members in the team
     */
    long countByTeamId(Long teamId);
    
    /**
     * Count team members by role within a team.
     * 
     * @param teamId The team ID
     * @param role The role to count
     * @return Number of members with the specified role in the team
     */
    long countByTeamIdAndRole(Long teamId, String role);
    
    /**
     * Check if a user is a member of a team with a specific role.
     * 
     * @param teamId The team ID
     * @param userId The user ID
     * @param role The role to check
     * @return true if user has the specified role in the team
     */
    @Query("SELECT CASE WHEN COUNT(tm) > 0 THEN true ELSE false END FROM TeamMember tm " +
           "WHERE tm.teamId = :teamId AND tm.userId = :userId AND tm.role = :role AND tm.status = 'ACTIVE'")
    boolean existsByTeamIdAndUserIdAndRole(@Param("teamId") Long teamId, 
                                          @Param("userId") Long userId, 
                                          @Param("role") String role);
    
    /**
     * Find all active team members by organization ID.
     * 
     * @param organizationId The organization ID
     * @return List of active team members in the organization
     */
    @Query("SELECT tm FROM TeamMember tm " +
           "JOIN Team t ON tm.teamId = t.id " +
           "WHERE t.organizationId = :organizationId AND tm.status = 'ACTIVE'")
    List<TeamMember> findByOrganizationIdAndStatus(@Param("organizationId") Long organizationId, 
                                                  String status);
}
