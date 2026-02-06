package io.zaplink.scheduler.dto.request;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for updating scheduled time.
 *
 * @param newTime The new time to schedule the post.
 */
public record UpdateTimeRequest( @NotNull(message = "New scheduled time is required") @JsonProperty("new_time") Instant newTime )
{
}
