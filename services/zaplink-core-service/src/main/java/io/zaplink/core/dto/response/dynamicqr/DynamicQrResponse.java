package io.zaplink.core.dto.response.dynamicqr;

import java.time.LocalDateTime;

public record DynamicQrResponse( Long id,
                                 String qrKey,
                                 String qrName,
                                 String currentDestinationUrl,
                                 String qrImageUrl,
                                 String redirectUrl,
                                 String campaignId,
                                 String userEmail,
                                 Boolean isActive,
                                 Long totalScans,
                                 LocalDateTime createdAt,
                                 LocalDateTime updatedAt,
                                 LocalDateTime lastScanned,
                                 // Advanced Fields
                                 Object qrConfig,
                                 Object allowedDomains,
                                 String password,
                                 Integer scanLimit,
                                 LocalDateTime expirationDate,
                                 Boolean trackAnalytics )
{
}
