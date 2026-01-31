package io.zaplink.core.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.zaplink.core.common.constants.DatabaseConstants;
import io.zaplink.core.common.enums.PostStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Post entity representing content created by team members.
 * Posts go through a workflow: Draft -> Submitted -> Approved/Rejected -> Published
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Entity @Table(name = DatabaseConstants.TABLE_POSTS) @EntityListeners(AuditingEntityListener.class) @Data @Builder @NoArgsConstructor @AllArgsConstructor @Accessors(chain = true)
public class Post
{
    /**
     * Primary key for the post.
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;
    /**
     * Title of the post.
     */
    @NotBlank @Size(max = 255) @Column(name = DatabaseConstants.COLUMN_TITLE, nullable = false)
    private String  title;
    /**
     * Content of the post.
     */
    @Column(name = DatabaseConstants.COLUMN_CONTENT, columnDefinition = "TEXT")
    private String  content;
    /**
     * Campaign ID this post belongs to (optional).
     */
    @Column(name = DatabaseConstants.COLUMN_CAMPAIGN_ID)
    private Long    campaignId;
    /**
     * User ID of the post author.
     */
    @NotNull @Column(name = DatabaseConstants.COLUMN_AUTHOR_ID, nullable = false)
    private Long    authorId;
    /**
     * Status of the post in the workflow.
     * Valid values: DRAFT, SUBMITTED, APPROVED, REJECTED, PUBLISHED
     */
    @Builder.Default @Column(name = DatabaseConstants.COLUMN_STATUS)
    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.DRAFT;
    /**
     * Timestamp when the post was submitted for approval.
     */
    @Column(name = DatabaseConstants.COLUMN_SUBMITTED_AT)
    private Instant submittedAt;
    /**
     * User ID of the reviewer who approved/rejected the post.
     */
    @Column(name = DatabaseConstants.COLUMN_REVIEWED_BY)
    private Long    reviewedBy;
    /**
     * Timestamp when the post was reviewed.
     */
    @Column(name = DatabaseConstants.COLUMN_REVIEWED_AT)
    private Instant reviewedAt;
    /**
     * Comments from the reviewer about the approval/rejection decision.
     */
    @Column(name = DatabaseConstants.COLUMN_REVIEW_COMMENTS, columnDefinition = "TEXT")
    private String  reviewComments;
    /**
     * Timestamp when the post was published.
     */
    @Column(name = DatabaseConstants.COLUMN_PUBLISHED_AT)
    private Instant publishedAt;
    /**
     * Timestamp when the post was created.
     */
    @CreatedDate @Column(name = DatabaseConstants.COLUMN_CREATED_AT, nullable = false, updatable = false)
    private Instant createdAt;
    /**
     * Timestamp when the post was last updated.
     */
    @LastModifiedDate @Column(name = DatabaseConstants.COLUMN_UPDATED_AT)
    private Instant updatedAt;
}
