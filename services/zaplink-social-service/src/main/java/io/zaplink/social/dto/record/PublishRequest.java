package io.zaplink.social.dto.record;

import java.util.UUID;

/**
 * Data Transfer Object for Publishing Requests.
 *
 * @param accountId The UUID of the connected social account.
 * @param caption   The text to publish.
 * @param mediaUrl  The URL of the media asset.
 */
public record PublishRequest( UUID accountId, String caption, String mediaUrl )
{
}
