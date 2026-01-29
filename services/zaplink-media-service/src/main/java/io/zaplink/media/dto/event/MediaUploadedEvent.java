package io.zaplink.media.dto.event;

import java.util.UUID;

public record MediaUploadedEvent( UUID assetId, UUID ownerId, String url, String mimeType, Long sizeBytes )
{
}
