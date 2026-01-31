package io.zaplink.manager.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDto( Long id,
                       String email,
                       String username,
                       @JsonProperty("first_name") String firstName,
                       @JsonProperty("last_name") String lastName,
                       @JsonProperty("created_at") LocalDateTime createdAt,
                       @JsonProperty("updated_at") LocalDateTime updatedAt )
{
}
