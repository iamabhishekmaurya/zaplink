package io.zaplink.core.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO for post information in responses.
 * Contains post details and workflow status.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {
    
    /**
     * Post ID.
     */
    private Long id;
    
    /**
     * Post title.
     */
    private String title;
    
    /**
     * Post content.
     */
    private String content;
    
    /**
     * Campaign ID if associated with a campaign.
     */
    private Long campaignId;
    
    /**
     * Campaign name if associated with a campaign.
     */
    private String campaignName;
    
    /**
     * Author ID.
     */
    private Long authorId;
    
    /**
     * Author name.
     */
    private String authorName;
    
    /**
     * Author email.
     */
    private String authorEmail;
    
    /**
     * Post status in workflow.
     */
    private String status;
    
    /**
     * When the post was submitted for approval.
     */
    private Instant submittedAt;
    
    /**
     * Reviewer ID if reviewed.
     */
    private Long reviewedBy;
    
    /**
     * Reviewer name if reviewed.
     */
    private String reviewerName;
    
    /**
     * When the post was reviewed.
     */
    private Instant reviewedAt;
    
    /**
     * Review comments.
     */
    private String reviewComments;
    
    /**
     * When the post was published.
     */
    private Instant publishedAt;
    
    /**
     * When the post was created.
     */
    private Instant createdAt;
    
    /**
     * When the post was last updated.
     */
    private Instant updatedAt;
}
