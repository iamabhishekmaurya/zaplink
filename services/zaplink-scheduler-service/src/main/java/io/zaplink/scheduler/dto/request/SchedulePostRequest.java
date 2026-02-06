package io.zaplink.scheduler.dto.request;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for scheduling a new post.
 * Used for creating new scheduled posts via the API.
 *
 * @param caption          The post caption text.
 * @param mediaAssetIds    List of media asset UUIDs to attach.
 * @param scheduledTime    The time to publish the post.
 * @param socialAccountIds List of social account UUIDs to post to.
 */
public record SchedulePostRequest( String caption,
                                   @JsonProperty("media_asset_ids") List<UUID> mediaAssetIds,
                                   @NotNull(message = "Scheduled time is required") @JsonAlias("scheduledAt") @JsonProperty("scheduled_time") Instant scheduledTime,
                                   @JsonProperty("social_account_ids") List<UUID> socialAccountIds )
{
}
