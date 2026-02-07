package io.zaplink.core.dto.response;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for post information in responses.
 * Contains post details and workflow status.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public record PostResponse( Long id,
                            String title,
                            String content,
                            @JsonProperty("campaign_id") Long campaignId,
                            @JsonProperty("campaign_name") String campaignName,
                            @JsonProperty("author_id") Long authorId,
                            @JsonProperty("author_name") String authorName,
                            @JsonProperty("author_email") String authorEmail,
                            String status,
                            @JsonProperty("submitted_at") Instant submittedAt,
                            @JsonProperty("reviewed_by") Long reviewedBy,
                            @JsonProperty("reviewer_name") String reviewerName,
                            @JsonProperty("reviewed_at") Instant reviewedAt,
                            @JsonProperty("review_comments") String reviewComments,
                            @JsonProperty("published_at") Instant publishedAt,
                            @JsonProperty("created_at") Instant createdAt,
                            @JsonProperty("updated_at") Instant updatedAt )
{
}
