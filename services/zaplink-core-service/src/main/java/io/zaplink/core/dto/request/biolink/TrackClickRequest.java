package io.zaplink.core.dto.request.biolink;

import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * Request DTO for tracking bio link clicks.
 * Contains analytics data such as referrer and user agent.
 */
@Builder
public record TrackClickRequest( @Size(max = 2048, message = "Referrer URL cannot exceed 2048 characters") String referrer,
                                 @Size(max = 1000, message = "User agent cannot exceed 1000 characters") String userAgent )
{
}
