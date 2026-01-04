package io.zaplink.core.dto.response.dynamicqr;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class QrAnalyticsResponse {
    
    private String qrKey;
    private String qrName;
    private Long totalScans;
    private Long scansToday;
    private Long scansThisWeek;
    private Long scansThisMonth;
    private LocalDateTime lastScanned;
    private List<CountryStats> countryStats;
    private List<DeviceStats> deviceStats;
    private List<BrowserStats> browserStats;
    private List<DailyStats> dailyStats;
    
    @Data
    public static class CountryStats {
        private String country;
        private Long count;
        private Double percentage;
    }
    
    @Data
    public static class DeviceStats {
        private String deviceType;
        private Long count;
        private Double percentage;
    }
    
    @Data
    public static class BrowserStats {
        private String browser;
        private Long count;
        private Double percentage;
    }
    
    @Data
    public static class DailyStats {
        private String date;
        private Long scans;
    }
}
