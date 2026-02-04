package io.zaplink.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import io.zaplink.core.entity.Organization;

/**
 * Repository for Organization entity operations.
 * Provides database access methods for organization management.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Repository
public interface OrganizationRepository
    extends
    JpaRepository<Organization, Long>
{
    /**
     * Find organization by owner ID.
     * 
     * @param ownerId The owner user ID
     * @return List of organizations owned by the user
     */
    List<Organization> findByOwnerId( Long ownerId );

    /**
     * Find organization by name.
     * 
     * @param name The organization name
     * @return Optional containing the organization if found
     */
    Optional<Organization> findByName( String name );

    /**
     * Find all active organizations.
     * 
     * @param active The active status
     * @return List of active organizations
     */
    List<Organization> findByActive( Boolean active );

    /**
     * Count organizations by owner ID.
     * 
     * @param ownerId The owner user ID
     * @return Number of organizations owned by the user
     */
    long countByOwnerId( Long ownerId );

    /**
     * Find organizations with team and member counts.
     * 
     * @return List of organizations with statistics
     */
    @Query("SELECT o, COUNT(DISTINCT t) as teamCount, COUNT(DISTINCT tm) as memberCount " + "FROM Organization o "
            + "LEFT JOIN Team t ON o.id = t.organizationId AND t.active = true "
            + "LEFT JOIN TeamMember tm ON t.id = tm.teamId AND tm.status = 'ACTIVE' " + "WHERE o.active = true "
            + "GROUP BY o.id ORDER BY o.name")
    List<Object[]> findOrganizationsWithStatistics();
}
