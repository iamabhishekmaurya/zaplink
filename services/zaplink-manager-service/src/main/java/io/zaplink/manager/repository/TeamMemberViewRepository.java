// package io.zaplink.manager.repository;
// import java.util.List;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;
// import io.zaplink.manager.entity.TeamMemberView;
// /**
//  * Repository for TeamMemberView entity operations.
//  * Provides read-optimized access to team member information.
//  * 
//  * @author Zaplink Team
//  * @version 1.0
//  * @since 2026-01-31
//  */
// @Repository
// public interface TeamMemberViewRepository
//     extends
//     JpaRepository<TeamMemberView, Long>
// {
//     /**
//      * Find team member view by team ID.
//      * 
//      * @param teamId The team ID
//      * @return List of team members in the team
//      */
//     List<TeamMemberView> findByTeamId( Long teamId );
//     /**
//      * Find team member view by user ID.
//      * 
//      * @param userId The user ID
//      * @return List of teams the user belongs to
//      */
//     List<TeamMemberView> findByUserId( Long userId );
//     /**
//      * Find team member view by organization ID.
//      * 
//      * @param organizationId The organization ID
//      * @return List of team members in the organization
//      */
//     List<TeamMemberView> findByOrganizationId( Long organizationId );
//     /**
//      * Find team member view by role.
//      * 
//      * @param role The role to search for
//      * @return List of team members with the specified role
//      */
//     List<TeamMemberView> findByRole( String role );
//     /**
//      * Find team member view by status.
//      * 
//      * @param status The status to search for
//      * @return List of team members with the specified status
//      */
//     List<TeamMemberView> findByStatus( String status );
//     /**
//      * Find team member view by team ID and role.
//      * 
//      * @param teamId The team ID
//      * @param role The role to search for
//      * @return List of team members in the team with the specified role
//      */
//     List<TeamMemberView> findByTeamIdAndRole( Long teamId, String role );
//     /**
//      * Find team member view by organization ID and role.
//      * 
//      * @param organizationId The organization ID
//      * @param role The role to search for
//      * @return List of team members in the organization with the specified role
//      */
//     List<TeamMemberView> findByOrganizationIdAndRole( Long organizationId, String role );
//     /**
//      * Find team member view by team ID and status.
//      * 
//      * @param teamId The team ID
//      * @param status The status to search for
//      * @return List of team members in the team with the specified status
//      */
//     List<TeamMemberView> findByTeamIdAndStatus( Long teamId, String status );
//     /**
//      * Count team members by team ID.
//      * 
//      * @param teamId The team ID
//      * @return Number of members in the team
//      */
//     long countByTeamId( Long teamId );
//     /**
//      * Count team members by organization ID.
//      * 
//      * @param organizationId The organization ID
//      * @return Number of members in the organization
//      */
//     long countByOrganizationId( Long organizationId );
//     /**
//      * Count team members by role within an organization.
//      * 
//      * @param organizationId The organization ID
//      * @param role The role to count
//      * @return Number of members with the specified role in the organization
//      */
//     long countByOrganizationIdAndRole( Long organizationId, String role );
//     /**
//      * Find all influencers in an organization.
//      * 
//      * @param organizationId The organization ID
//      * @return List of influencers in the organization
//      */
//     @Query("SELECT tmv FROM TeamMemberView tmv "
//             + "WHERE tmv.organizationId = :organizationId AND tmv.role = 'INFLUENCER' AND tmv.status = 'ACTIVE'")
//     List<TeamMemberView> findInfluencersByOrganization( @Param("organizationId") Long organizationId );
// }
