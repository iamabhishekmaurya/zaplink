package io.zaplink.manager.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OrganizationDto( Long id,
                               String name,
                               String description,
                               @JsonProperty("owner_id") Long ownerId,
                               String settings,
                               @JsonProperty("created_at") LocalDateTime createdAt,
                               @JsonProperty("updated_at") LocalDateTime updatedAt )
{
}
