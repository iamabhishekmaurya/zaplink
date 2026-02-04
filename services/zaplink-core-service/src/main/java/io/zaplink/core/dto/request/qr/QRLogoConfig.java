package io.zaplink.core.dto.request.qr;

public record QRLogoConfig( String logoPath,
                            double sizeRatio, // Logo size as ratio of QR size (0.1 to 0.3)
                            int padding, // Padding around logo (reduced from 8)
                            String backgroundColor, // Background color for logo
                            boolean backgroundEnabled, // Whether to draw background
                            boolean backgroundRounded, // Rounded background corners
                            int backgroundCornerRadius, // Corner radius for background
                            boolean removeQuietZone, // Remove QR modules behind logo
                            int marginSize// Margin around logo where modules are removed (default 0 for tight fit)
)
{
    public QRLogoConfig()
    {
        this( null, 0.2, 2, "#FFFFFF", true, true, 20, true, 0 );
    }

    public QRLogoConfig( String logoPath,
                         double sizeRatio,
                         int padding,
                         String backgroundColor,
                         boolean backgroundEnabled,
                         boolean backgroundRounded,
                         int backgroundCornerRadius,
                         boolean removeQuietZone,
                         int marginSize )
    {
        this.logoPath = logoPath;
        this.sizeRatio = sizeRatio;
        this.padding = padding;
        this.backgroundColor = backgroundColor;
        this.backgroundEnabled = backgroundEnabled;
        this.backgroundRounded = backgroundRounded;
        this.backgroundCornerRadius = backgroundCornerRadius;
        this.removeQuietZone = removeQuietZone;
        this.marginSize = marginSize;
    }
}
