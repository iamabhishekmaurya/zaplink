package io.zaplink.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.zaplink.core.entity.Team;

/**
 * Repository for Team entity operations.
 * Provides database access methods for team management.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Repository
public interface TeamRepository
    extends
    JpaRepository<Team, Long>
{
    /**
     * Find all teams by organization ID.
     * 
     * @param organizationId The organization ID
     * @return List of teams in the organization
     */
    List<Team> findByOrganizationId( Long organizationId );

    /**
     * Find all teams by organization ID and active status.
     * 
     * @param organizationId The organization ID
     * @param active The active status
     * @return List of active teams in the organization
     */
    List<Team> findByOrganizationIdAndActive( Long organizationId, Boolean active );

    /**
     * Find team by name and organization ID.
     * 
     * @param name The team name
     * @param organizationId The organization ID
     * @return Optional containing the team if found
     */
    Optional<Team> findByNameAndOrganizationId( String name, Long organizationId );

    /**
     * Count teams by organization ID.
     * 
     * @param organizationId The organization ID
     * @return Number of teams in the organization
     */
    long countByOrganizationId( Long organizationId );

    /**
     * Count active teams by organization ID.
     * 
     * @param organizationId The organization ID
     * @return Number of active teams in the organization
     */
    long countByOrganizationIdAndActive( Long organizationId, Boolean active );

    /**
     * Find teams with member count for an organization.
     * 
     * @param organizationId The organization ID
     * @return List of teams with member count
     */
    @Query("SELECT t, COUNT(tm) as memberCount FROM Team t "
            + "LEFT JOIN TeamMember tm ON t.id = tm.teamId AND tm.status = 'ACTIVE' "
            + "WHERE t.organizationId = :organizationId AND t.active = true " + "GROUP BY t.id ORDER BY t.name")
    List<Object[]> findTeamsWithMemberCountByOrganization( @Param("organizationId") Long organizationId );
}
