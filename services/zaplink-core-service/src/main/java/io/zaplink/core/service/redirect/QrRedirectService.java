package io.zaplink.core.service.redirect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface QrRedirectService {
    
    boolean handleQrRedirect(String qrKey, HttpServletRequest request, HttpServletResponse response);
    
    void trackQrScan(String qrKey, HttpServletRequest request);
}
