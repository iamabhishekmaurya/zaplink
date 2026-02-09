// package io.zaplink.manager.entity;
// import java.time.Instant;
// import io.zaplink.manager.common.constants.DatabaseConstants;
// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.Id;
// import jakarta.persistence.Table;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import lombok.experimental.Accessors;
// /**
//  * Entity representing the team member read model.
//  * This is a materialized view optimized for querying team member information.
//  * 
//  * @author Zaplink Team
//  * @version 1.0
//  * @since 2026-01-31
//  */
// @Entity @Table(name = DatabaseConstants.TABLE_TEAM_MEMBER_VIEW) @Data @Builder @NoArgsConstructor @AllArgsConstructor @Accessors(chain = true)
// public class TeamMemberView
// {
//     /**
//      * Primary key for the team member view.
//      */
//     @Id
//     private Long    id;
//     /**
//      * Team ID.
//      */
//     @Column(name = DatabaseConstants.COLUMN_TEAM_ID, nullable = false)
//     private Long    teamId;
//     /**
//      * Team name.
//      */
//     @Column(name = DatabaseConstants.COLUMN_TEAM_NAME, nullable = false)
//     private String  teamName;
//     /**
//      * User ID.
//      */
//     @Column(name = DatabaseConstants.COLUMN_USER_ID, nullable = false)
//     private Long    userId;
//     /**
//      * Username.
//      */
//     @Column(name = DatabaseConstants.COLUMN_USERNAME, nullable = false)
//     private String  username;
//     /**
//      * User email.
//      */
//     @Column(name = DatabaseConstants.COLUMN_USER_EMAIL, nullable = false)
//     private String  userEmail;
//     /**
//      * User first name.
//      */
//     @Column(name = DatabaseConstants.COLUMN_FIRST_NAME)
//     private String  firstName;
//     /**
//      * User last name.
//      */
//     @Column(name = DatabaseConstants.COLUMN_LAST_NAME)
//     private String  lastName;
//     /**
//      * Role within the team.
//      */
//     @Column(name = DatabaseConstants.COLUMN_ROLE, nullable = false)
//     private String  role;
//     /**
//      * Status of the team member.
//      */
//     @Column(name = DatabaseConstants.COLUMN_STATUS, nullable = false)
//     private String  status;
//     /**
//      * When the member was invited.
//      */
//     @Column(name = DatabaseConstants.COLUMN_INVITED_AT)
//     private Instant invitedAt;
//     /**
//      * When the member joined the team.
//      */
//     @Column(name = DatabaseConstants.COLUMN_JOINED_AT)
//     private Instant joinedAt;
//     /**
//      * Organization ID.
//      */
//     @Column(name = DatabaseConstants.COLUMN_ORGANIZATION_ID, nullable = false)
//     private Long    organizationId;
//     /**
//      * Organization name.
//      */
//     @Column(name = DatabaseConstants.COLUMN_ORGANIZATION_NAME, nullable = false)
//     private String  organizationName;
//     /**
//      * When the record was last updated.
//      */
//     @Column(name = DatabaseConstants.COLUMN_LAST_UPDATED, nullable = false)
//     private Instant lastUpdated;
// }
