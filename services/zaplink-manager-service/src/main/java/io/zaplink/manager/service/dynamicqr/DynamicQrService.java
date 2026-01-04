package io.zaplink.manager.service.dynamicqr;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.zaplink.manager.dto.response.dynamicqr.DynamicQrResponse;
import io.zaplink.manager.dto.response.dynamicqr.QrAnalyticsResponse;

public interface DynamicQrService
{
    Optional<DynamicQrResponse> getDynamicQr( String qrKey, String userEmail );

    Page<DynamicQrResponse> getDynamicQrsByUser( String userEmail, Pageable pageable );

    byte[] generateQrImage( String qrKey, String userEmail );

    QrAnalyticsResponse getQrAnalytics( String qrKey,
                                        String userEmail,
                                        LocalDateTime startDate,
                                        LocalDateTime endDate );

    List<DynamicQrResponse> getQrCodesByCampaign( String campaignId, String userEmail );

    long countAllQrCodes();
}
