package io.zaplink.core.service.dynamicqr;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import io.zaplink.core.dto.request.dynamicqr.CreateDynamicQrRequest;
import io.zaplink.core.dto.request.dynamicqr.UpdateDestinationRequest;
import io.zaplink.core.dto.response.dynamicqr.DynamicQrResponse;
import io.zaplink.core.dto.response.dynamicqr.QrAnalyticsResponse;

public interface DynamicQrService
{
    DynamicQrResponse createDynamicQr( CreateDynamicQrRequest request, String userEmail );

    Optional<DynamicQrResponse> getDynamicQr( String qrKey, String userEmail );

    Page<DynamicQrResponse> getDynamicQrsByUser( String userEmail, Pageable pageable );

    DynamicQrResponse updateDestination( String qrKey, UpdateDestinationRequest request, String userEmail );

    void toggleQrStatus( String qrKey, String userEmail );

    void deleteDynamicQr( String qrKey, String userEmail );

    byte[] generateQrImage( String qrKey, String userEmail );

    QrAnalyticsResponse getQrAnalytics( String qrKey,
                                        String userEmail,
                                        LocalDateTime startDate,
                                        LocalDateTime endDate );

    List<DynamicQrResponse> getQrCodesByCampaign( String campaignId, String userEmail );

    long countAllQrCodes();
}
