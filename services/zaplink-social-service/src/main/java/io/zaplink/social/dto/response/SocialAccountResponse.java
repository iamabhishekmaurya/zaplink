package io.zaplink.social.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.social.common.enums.SocialProvider;
import io.zaplink.social.entity.SocialAccount;

/**
 * Response DTO for Social Account.
 * Excludes sensitive fields like access_token and refresh_token.
 */
public record SocialAccountResponse( UUID id,
                                     SocialProvider provider,
                                     @JsonProperty("provider_id") String providerId,
                                     @JsonProperty("token_expiry") LocalDateTime tokenExpiry,
                                     @JsonProperty("owner_id") UUID ownerId,
                                     @JsonProperty("created_at") LocalDateTime createdAt,
                                     @JsonProperty("updated_at") LocalDateTime updatedAt )
{
    /**
     * Factory method to create a response DTO from the entity.
     *
     * @param entity The SocialAccount entity.
     * @return The SocialAccountResponse DTO.
     */
    public static SocialAccountResponse from( SocialAccount entity )
    {
        if ( entity == null )
        {
            return null;
        }
        return new SocialAccountResponse( entity.getId(),
                                          entity.getProvider(),
                                          entity.getProviderId(),
                                          entity.getTokenExpiry(),
                                          entity.getOwnerId(),
                                          entity.getCreatedAt(),
                                          entity.getUpdatedAt() );
    }
}
