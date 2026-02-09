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
//  * Entity representing the influencer campaign read model.
//  * This is a materialized view optimized for querying influencer campaign assignments.
//  * 
//  * @author Zaplink Team
//  * @version 1.0
//  * @since 2026-01-31
//  */
// @Entity @Table(name = DatabaseConstants.TABLE_INFLUENCER_CAMPAIGN_VIEW) @Data @Builder @NoArgsConstructor @AllArgsConstructor @Accessors(chain = true)
// public class InfluencerCampaignView
// {
//     /**
//      * Primary key for the influencer campaign view.
//      */
//     @Id
//     private Long    id;
//     /**
//      * Campaign ID.
//      */
//     @Column(name = DatabaseConstants.COLUMN_CAMPAIGN_ID, nullable = false)
//     private Long    campaignId;
//     /**
//      * Campaign name.
//      */
//     @Column(name = DatabaseConstants.COLUMN_CAMPAIGN_NAME, nullable = false)
//     private String  campaignName;
//     /**
//      * Campaign description.
//      */
//     @Column(name = DatabaseConstants.COLUMN_DESCRIPTION, columnDefinition = "TEXT")
//     private String  campaignDescription;
//     /**
//      * Campaign status.
//      */
//     @Column(name = DatabaseConstants.COLUMN_CAMPAIGN_STATUS, nullable = false)
//     private String  campaignStatus;
//     /**
//      * Campaign start date.
//      */
//     @Column(name = DatabaseConstants.COLUMN_START_DATE)
//     private Instant startDate;
//     /**
//      * Campaign end date.
//      */
//     @Column(name = DatabaseConstants.COLUMN_END_DATE)
//     private Instant endDate;
//     /**
//      * Team member ID (influencer).
//      */
//     @Column(name = DatabaseConstants.COLUMN_TEAM_MEMBER_ID, nullable = false)
//     private Long    teamMemberId;
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
//      * Assignment status.
//      */
//     @Column(name = DatabaseConstants.COLUMN_ASSIGNMENT_STATUS, nullable = false)
//     private String  assignmentStatus;
//     /**
//      * When the influencer was assigned to the campaign.
//      */
//     @Column(name = DatabaseConstants.COLUMN_ASSIGNED_AT, nullable = false)
//     private Instant assignedAt;
//     /**
//      * When the influencer completed the campaign.
//      */
//     @Column(name = DatabaseConstants.COLUMN_COMPLETED_AT)
//     private Instant completedAt;
//     /**
//      * When the record was last updated.
//      */
//     @Column(name = DatabaseConstants.COLUMN_LAST_UPDATED, nullable = false)
//     private Instant lastUpdated;
// }
