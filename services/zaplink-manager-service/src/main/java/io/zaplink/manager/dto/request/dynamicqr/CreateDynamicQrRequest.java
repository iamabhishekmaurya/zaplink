package io.zaplink.manager.dto.request.dynamicqr;

import io.zaplink.manager.dto.request.qr.QRConfig;
import lombok.Data;

@Data
public class CreateDynamicQrRequest
{
    private String                  qrName;
    private String                  destinationUrl;
    private QRConfig                qrConfig;
    private String                  campaignId;
    private String                  password;       // Optional
    private Integer                 scanLimit;      // Optional
    private java.time.LocalDateTime expirationDate; // Optional
    private java.util.List<String>  allowedDomains; // Optional
}
