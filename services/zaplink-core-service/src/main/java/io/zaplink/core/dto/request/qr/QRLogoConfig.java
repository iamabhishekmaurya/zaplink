package io.zaplink.core.dto.request.qr;

import lombok.Data;

@Data
public class QRLogoConfig
{
    private String  logoPath               = null;
    private double  sizeRatio              = 0.2;       // Logo size as ratio of QR size (0.1 to 0.3)
    private int     padding                = 2;         // Padding around logo (reduced from 8)
    private String  backgroundColor        = "#FFFFFF"; // Background color for logo
    private boolean backgroundEnabled      = true;      // Whether to draw background
    private boolean backgroundRounded      = true;      // Rounded background corners
    private int     backgroundCornerRadius = 20;        // Corner radius for background
    private boolean removeQuietZone        = true;      // Remove QR modules behind logo
    private int     marginSize             = 0;         // Margin around logo where modules are removed (default 0 for tight fit)
}
