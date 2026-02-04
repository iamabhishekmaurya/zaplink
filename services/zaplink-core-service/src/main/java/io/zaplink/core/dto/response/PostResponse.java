package io.zaplink.core.dto.response;

import java.time.Instant;

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
                            Long campaignId,
                            String campaignName,
                            Long authorId,
                            String authorName,
                            String authorEmail,
                            String status,
                            Instant submittedAt,
                            Long reviewedBy,
                            String reviewerName,
                            Instant reviewedAt,
                            String reviewComments,
                            Instant publishedAt,
                            Instant createdAt,
                            Instant updatedAt )
{
}
