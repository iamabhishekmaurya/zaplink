package io.zaplink.scheduler.dto.response;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.scheduler.entity.ScheduledPost;

/**
 * Response DTO for scheduled post data.
 *
 * @param id               The unique identifier of the post.
 * @param caption          The post caption text.
 * @param mediaAssetIds    List of media asset UUIDs attached.
 * @param scheduledTime    The scheduled publication time.
 * @param status           The current status of the post.
 * @param socialAccountIds List of social account UUIDs.
 * @param ownerId          The owner's ID.
 * @param createdAt        Creation timestamp.
 * @param updatedAt        Last update timestamp.
 */
public record ScheduledPostResponse( UUID id,
                                     String caption,
                                     @JsonProperty("media_asset_ids") List<UUID> mediaAssetIds,
                                     @JsonProperty("scheduled_time") Instant scheduledTime,
                                     String status,
                                     @JsonProperty("social_account_ids") List<UUID> socialAccountIds,
                                     @JsonProperty("owner_id") String ownerId,
                                     @JsonProperty("created_at") LocalDateTime createdAt,
                                     @JsonProperty("updated_at") LocalDateTime updatedAt )
{
    /**
     * Factory method to convert entity to response DTO.
     */
    public static ScheduledPostResponse from( ScheduledPost entity )
    {
        return new ScheduledPostResponse( entity.getId(),
                                          entity.getCaption(),
                                          entity.getMediaAssetIds(),
                                          entity.getScheduledTime(),
                                          entity.getStatus() != null ? entity.getStatus().name() : null,
                                          entity.getSocialAccountIds(),
                                          entity.getOwnerId(),
                                          entity.getCreatedAt(),
                                          entity.getUpdatedAt() );
    }
}
