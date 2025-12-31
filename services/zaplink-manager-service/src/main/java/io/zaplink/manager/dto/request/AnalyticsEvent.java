package io.zaplink.manager.dto.request;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString @Builder @NoArgsConstructor @AllArgsConstructor
public class AnalyticsEvent
    extends
    BaseRequest
{
    private String        urlKey;
    private String        ipAddress;
    private String        userAgent;
    private String        referrer;
    private LocalDateTime timestamp;
}
