package io.zaplink.core.service.dynamicqr;

import io.zaplink.core.dto.request.dynamicqr.CreateDynamicQrRequest;
import io.zaplink.core.dto.request.dynamicqr.UpdateDestinationRequest;
import io.zaplink.core.dto.response.dynamicqr.DynamicQrResponse;

public interface DynamicQrService
{
    DynamicQrResponse createDynamicQr( CreateDynamicQrRequest request, String userEmail );

    DynamicQrResponse updateDestination( String qrKey, UpdateDestinationRequest request, String userEmail );

    void toggleQrStatus( String qrKey, String userEmail );

    void deleteDynamicQr( String qrKey, String userEmail );

    DynamicQrResponse updateDynamicQr( String qrKey,
                                       io.zaplink.core.dto.request.dynamicqr.UpdateDynamicQrRequest request,
                                       String userEmail );
}
