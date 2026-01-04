package io.zaplink.core.service.redirect.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zaplink.core.entity.DynamicQrCodeEntity;
import io.zaplink.core.entity.QrScanAnalyticsEntity;
import io.zaplink.core.repository.DynamicQrCodeRepository;
import io.zaplink.core.repository.QrScanAnalyticsRepository;
import io.zaplink.core.service.redirect.QrRedirectService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrRedirectServiceImpl implements QrRedirectService {
    
    private final DynamicQrCodeRepository dynamicQrCodeRepository;
    private final QrScanAnalyticsRepository qrScanAnalyticsRepository;
    
    @Override
    @Transactional
    public boolean handleQrRedirect(String qrKey, HttpServletRequest request, HttpServletResponse response) {
        try {
            Optional<DynamicQrCodeEntity> qrOpt = dynamicQrCodeRepository.findByQrKey(qrKey);
            
            if (qrOpt.isEmpty() || !qrOpt.get().getIsActive()) {
                log.warn("QR code not found or inactive: {}", qrKey);
                return false;
            }
            
            DynamicQrCodeEntity qrEntity = qrOpt.get();
            
            // Track the scan asynchronously
            trackQrScan(qrKey, request);
            
            // Update scan count and last scanned timestamp
            qrEntity.setTotalScans(qrEntity.getTotalScans() + 1);
            qrEntity.setLastScanned(LocalDateTime.now());
            dynamicQrCodeRepository.save(qrEntity);
            
            // Redirect to destination
            response.sendRedirect(qrEntity.getCurrentDestinationUrl());
            return true;
            
        } catch (Exception e) {
            log.error("Error handling QR redirect for key: {}", qrKey, e);
            return false;
        }
    }
    
    @Override
    @Transactional
    public void trackQrScan(String qrKey, HttpServletRequest request) {
        try {
            String ipAddress = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");
            String referrer = request.getHeader("Referer");
            
            // Extract device and browser info from user agent
            String deviceType = extractDeviceType(userAgent);
            String browser = extractBrowser(userAgent);
            
            // TODO: Implement geolocation service to get country and city
            String country = "Unknown";
            String city = "Unknown";
            
            QrScanAnalyticsEntity analytics = QrScanAnalyticsEntity.builder()
                    .qrKey(qrKey)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .referrer(referrer)
                    .country(country)
                    .city(city)
                    .deviceType(deviceType)
                    .browser(browser)
                    .scannedAt(LocalDateTime.now())
                    .build();
            
            qrScanAnalyticsRepository.save(analytics);
            log.debug("Tracked scan for QR key: {} from IP: {}", qrKey, ipAddress);
            
        } catch (Exception e) {
            log.error("Error tracking QR scan for key: {}", qrKey, e);
            // Don't throw exception to avoid affecting the redirect
        }
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
    
    private String extractDeviceType(String userAgent) {
        if (userAgent == null) return "Unknown";
        
        userAgent = userAgent.toLowerCase();
        
        if (userAgent.contains("mobile") || userAgent.contains("android") || userAgent.contains("iphone") || userAgent.contains("ipad")) {
            if (userAgent.contains("tablet") || userAgent.contains("ipad")) {
                return "Tablet";
            }
            return "Mobile";
        }
        
        return "Desktop";
    }
    
    private String extractBrowser(String userAgent) {
        if (userAgent == null) return "Unknown";
        
        userAgent = userAgent.toLowerCase();
        
        if (userAgent.contains("chrome")) return "Chrome";
        if (userAgent.contains("firefox")) return "Firefox";
        if (userAgent.contains("safari")) return "Safari";
        if (userAgent.contains("edge")) return "Edge";
        if (userAgent.contains("opera")) return "Opera";
        
        return "Other";
    }
}
