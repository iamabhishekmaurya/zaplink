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
//  * Entity representing the pending post read model.
//  * This is a materialized view optimized for querying posts pending approval.
//  * 
//  * @author Zaplink Team
//  * @version 1.0
//  * @since 2026-01-31
//  */
// @Entity @Table(name = DatabaseConstants.TABLE_PENDING_POST_VIEW) @Data @Builder @NoArgsConstructor @AllArgsConstructor @Accessors(chain = true)
// public class PendingPostView
// {
//     /**
//      * Primary key for the pending post view.
//      */
//     @Id
//     private Long    id;
//     /**
//      * Post title.
//      */
//     @Column(name = DatabaseConstants.COLUMN_TITLE, nullable = false)
//     private String  title;
//     /**
//      * Post content.
//      */
//     @Column(name = DatabaseConstants.COLUMN_CONTENT, columnDefinition = "TEXT")
//     private String  content;
//     /**
//      * Author ID.
//      */
//     @Column(name = DatabaseConstants.COLUMN_AUTHOR_ID, nullable = false)
//     private Long    authorId;
//     /**
//      * Author name.
//      */
//     @Column(name = DatabaseConstants.COLUMN_AUTHOR_NAME, nullable = false)
//     private String  authorName;
//     /**
//      * Author email.
//      */
//     @Column(name = DatabaseConstants.COLUMN_AUTHOR_EMAIL, nullable = false)
//     private String  authorEmail;
//     /**
//      * Campaign ID if associated with a campaign.
//      */
//     @Column(name = DatabaseConstants.COLUMN_CAMPAIGN_ID)
//     private Long    campaignId;
//     /**
//      * Campaign name if associated with a campaign.
//      */
//     @Column(name = DatabaseConstants.COLUMN_CAMPAIGN_NAME)
//     private String  campaignName;
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
//      * When the post was submitted for approval.
//      */
//     @Column(name = DatabaseConstants.COLUMN_SUBMITTED_AT)
//     private Instant submittedAt;
//     /**
//      * Post status in workflow.
//      */
//     @Column(name = DatabaseConstants.COLUMN_STATUS, nullable = false)
//     private String  status;
//     /**
//      * When the record was last updated.
//      */
//     @Column(name = DatabaseConstants.COLUMN_LAST_UPDATED, nullable = false)
//     private Instant lastUpdated;
// }
