package io.zaplink.core.service.qr.engine;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import com.google.zxing.common.BitMatrix;

import io.zaplink.core.common.enums.QRBodyShapeEnum;
import io.zaplink.core.common.enums.QREyeShapeEnum;
import io.zaplink.core.dto.request.qr.QRBodyConfig;
import io.zaplink.core.dto.request.qr.QRConfig;
import io.zaplink.core.dto.request.qr.QREyeConfig;
import io.zaplink.core.dto.request.qr.QRLogoConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Slf4j @Component @Primary
public class AdvancedQRRenderer
    implements
    QRRenderer
{
    @Override
    public BufferedImage render( BitMatrix matrix, QRConfig config )
    {
        log.info( "Starting QR rendering with config: {}", config );
        int matrixSize = matrix.getWidth();
        int imageSize = config.getSize();
        // Use integer module size to prevent gaps/overlaps
        int moduleSize = imageSize / matrixSize;
        // Recalculate actual image size to fit modules exactly
        int actualImageSize = moduleSize * matrixSize;
        
        // If we lost pixels due to integer division, add them back to achieve exact size
        int offsetX = 0;
        int offsetY = 0;
        if (actualImageSize < imageSize) {
            int extraPixels = imageSize - actualImageSize;
            // Distribute extra pixels evenly around the QR
            offsetX = extraPixels / 2;
            offsetY = extraPixels / 2;
            log.info( "Adding {} extra pixels ({} each side) to achieve exact size", extraPixels, offsetX );
            actualImageSize = imageSize;
        }
        
        log.info( "Matrix size: {}, Image size: {}, Module size: {} (integer), Offset: X={}, Y={}", 
                  matrixSize, actualImageSize, moduleSize, offsetX, offsetY );
        // Pre-load logo to determine if we should apply logo logic
        BufferedImage logoConfigured = null;
        if ( config.getLogo() != null && config.getLogo().getLogoPath() != null
                && !config.getLogo().getLogoPath().isEmpty() )
        {
            logoConfigured = loadLogo( config.getLogo() );
        }
        final BufferedImage logo = logoConfigured;
        BufferedImage image = new BufferedImage( actualImageSize, actualImageSize, BufferedImage.TYPE_INT_RGB );
        Graphics2D g2d = image.createGraphics();
        // IMPORTANT: Disable anti-aliasing for QR codes - scanners need crisp module edges
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF );
        g2d.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED );
        // Draw background
        drawBackground( g2d, config, actualImageSize );
        // Check if we want styled eyes
        boolean customEyes = config.getEye() != null && config.getEye().getShape() != QREyeShapeEnum.SQUARE;
        // Draw QR body - skip finder patterns if we're drawing them separately with custom eyes
        drawBodyInteger( g2d, matrix, config, moduleSize, actualImageSize, logo != null, customEyes, offsetX, offsetY );
        // Draw eye patterns separately if custom styling requested
        if ( customEyes )
        {
            drawEyesInteger( g2d, matrix, config, moduleSize, offsetX, offsetY );
        }
        // Draw logo if provided and loaded
        if ( logo != null )
        {
            drawLogoImage( g2d, config, actualImageSize, logo );
        }
        // Draw decorative border
        drawBorder( g2d, config, actualImageSize );
        g2d.dispose();
        log.info( "QR rendering completed successfully | Size: {}px | Shapes: {}/{}", actualImageSize,
                  config.getBody().getShape(), config.getEye().getShape() );
        return image;
    }

    private void drawBackground( Graphics2D g2d, QRConfig config, int size )
    {
        log.debug( "Drawing background with color: {}", config.getBackgroundColor() );
        if ( config.isTransparentBackground() )
        {
            return;
        }
        Color bgColor = parseColor( config.getBackgroundColor() );
        g2d.setColor( bgColor );
        g2d.fillRect( 0, 0, size, size );
    }

    private void drawBody( Graphics2D g2d,
                           BitMatrix matrix,
                           QRConfig config,
                           double moduleSize,
                           int imageSize,
                           boolean hasLogo )
    {
        int matrixSize = matrix.getWidth();
        QRBodyConfig bodyConfig = config.getBody();
        int modulesDrawn = 0;
        // Prepare Paint (Solid or Gradient)
        Paint bodyPaint = getBodyPaint( bodyConfig, imageSize );
        g2d.setPaint( bodyPaint );
        for ( int y = 0; y < matrixSize; y++ )
        {
            for ( int x = 0; x < matrixSize; x++ )
            {
                if ( matrix.get( x, y ) )
                {
                    // Skip finder patterns
                    if ( isFinderPattern( x, y, matrixSize ) )
                    {
                        continue;
                    }
                    // Skip logo area if logo is present and loaded
                    if ( hasLogo && shouldSkipForLogo( x, y, matrixSize, config ) )
                    {
                        continue;
                    }
                    drawModule( g2d, bodyConfig.getShape(), x, y, moduleSize, matrix );
                    modulesDrawn++;
                }
            }
        }
        log.debug( "Drew {} body modules", modulesDrawn );
    }

    // Integer-based body drawing with styled modules
    private void drawBodyInteger( Graphics2D g2d,
                                  BitMatrix matrix,
                                  QRConfig config,
                                  int moduleSize,
                                  int imageSize,
                                  boolean hasLogo,
                                  boolean skipFinderPatterns,
                                  int offsetX,
                                  int offsetY )
    {
        int matrixSize = matrix.getWidth();
        int margin = config.getMargin();
        QRBodyConfig bodyConfig = config.getBody();
        QRBodyShapeEnum shape = bodyConfig.getShape();
        int modulesDrawn = 0;
        // Prepare Paint (Solid or Gradient)
        Paint bodyPaint = getBodyPaint( bodyConfig, imageSize );
        g2d.setPaint( bodyPaint );
        for ( int y = 0; y < matrixSize; y++ )
        {
            for ( int x = 0; x < matrixSize; x++ )
            {
                if ( matrix.get( x, y ) )
                {
                    // Skip logo area if logo is present
                    if ( hasLogo && shouldSkipForLogoPixel( x, y, moduleSize, imageSize, config ) )
                    {
                        continue;
                    }
                    // Skip finder patterns if we are drawing custom styled eyes
                    if ( skipFinderPatterns && isFinderPattern( x, y, matrixSize, margin ) )
                    {
                        continue;
                    }
                    int px = x * moduleSize + offsetX;
                    int py = y * moduleSize + offsetY;
                    // If we're NOT skipping finder patterns (using squares), draw them here
                    if ( isFinderPattern( x, y, matrixSize, margin ) )
                    {
                        g2d.fillRect( px, py, moduleSize, moduleSize );
                    }
                    else
                    {
                        // Draw normal body modules with style
                        drawStyledModuleInteger( g2d, shape, px, py, moduleSize, x, y, matrix );
                    }
                    modulesDrawn++;
                }
            }
        }
        log.debug( "Drew {} modules (styled body mode)", modulesDrawn );
    }

    // Draw a single styled module using integer coordinates
    private void drawStyledModuleInteger( Graphics2D g2d,
                                          QRBodyShapeEnum shape,
                                          int px,
                                          int py,
                                          int size,
                                          int x,
                                          int y,
                                          BitMatrix matrix )
    {
        switch ( shape )
        {
            case SQUARE:
                g2d.fillRect( px, py, size, size );
                break;
            case ROUNDED:
                int arc = size / 2;
                g2d.fillRoundRect( px, py, size, size, arc, arc );
                break;
            case CIRCLE:
            case DOT:
                g2d.fillOval( px, py, size, size );
                break;
            case LIQUID:
                drawLiquidModuleInteger( g2d, x, y, px, py, size, matrix );
                break;
            default:
                g2d.fillRect( px, py, size, size );
                break;
        }
    }

    // Liquid module with neighbor-aware corners (integer version)
    private void drawLiquidModuleInteger( Graphics2D g2d, int x, int y, int px, int py, int size, BitMatrix matrix )
    {
        boolean nN = y > 0 && matrix.get( x, y - 1 );
        boolean nS = y < matrix.getHeight() - 1 && matrix.get( x, y + 1 );
        boolean nW = x > 0 && matrix.get( x - 1, y );
        boolean nE = x < matrix.getWidth() - 1 && matrix.get( x + 1, y );
        int r = size / 2;
        // If isolated, draw as circle
        if ( !nN && !nS && !nW && !nE )
        {
            g2d.fillOval( px, py, size, size );
            return;
        }
        // Draw as a custom path for liquid effect
        GeneralPath path = new GeneralPath();
        // Start at middle left
        path.moveTo( px, py + r );
        // TL
        if ( !nN && !nW )
        {
            path.quadTo( px, py, px + r, py );
        }
        else
        {
            path.lineTo( px, py );
            path.lineTo( px + r, py );
        }
        // TR
        if ( !nN && !nE )
        {
            path.lineTo( px + size - r, py );
            path.quadTo( px + size, py, px + size, py + r );
        }
        else
        {
            path.lineTo( px + size, py );
            path.lineTo( px + size, py + r );
        }
        // BR
        if ( !nS && !nE )
        {
            path.lineTo( px + size, py + size - r );
            path.quadTo( px + size, py + size, px + size - r, py + size );
        }
        else
        {
            path.lineTo( px + size, py + size );
            path.lineTo( px + size - r, py + size );
        }
        // BL
        if ( !nS && !nW )
        {
            path.lineTo( px + r, py + size );
            path.quadTo( px, py + size, px, py + size - r );
        }
        else
        {
            path.lineTo( px, py + size );
            path.lineTo( px, py + size - r );
        }
        path.closePath();
        g2d.fill( path );
    }

    // Integer-based eye drawing
    private void drawEyesInteger( Graphics2D g2d, BitMatrix matrix, QRConfig config, int moduleSize, int offsetX, int offsetY )
    {
        int matrixSize = matrix.getWidth();
        int margin = config.getMargin();
        QREyeConfig eyeConfig = config.getEye();
        // Draw three finder patterns at the correct margin-aware locations
        drawEyeInteger( g2d, margin, margin, eyeConfig, moduleSize, offsetX, offsetY );
        drawEyeInteger( g2d, matrixSize - margin - 7, margin, eyeConfig, moduleSize, offsetX, offsetY );
        drawEyeInteger( g2d, margin, matrixSize - margin - 7, eyeConfig, moduleSize, offsetX, offsetY );
    }

    private void drawEyeInteger( Graphics2D g2d, int startX, int startY, QREyeConfig eyeConfig, int moduleSize, int offsetX, int offsetY )
    {
        int px = startX * moduleSize + offsetX;
        int py = startY * moduleSize + offsetY;
        int size = 7 * moduleSize;
        Color outerColor = parseColor( eyeConfig.getColorOuter() );
        Color innerColor = parseColor( eyeConfig.getColorInner() );
        QREyeShapeEnum shape = eyeConfig.getShape();
        // Outer Frame (7x7 with 5x5 hole)
        g2d.setColor( outerColor );
        Shape outerShape = createEyeFrame( shape, px, py, size, moduleSize );
        g2d.fill( outerShape );
        // Inner Dot (3x3 at center)
        g2d.setColor( innerColor );
        int dotSize = 3 * moduleSize;
        int dotOff = 2 * moduleSize;
        Shape innerShape = createEyeDot( shape, px + dotOff, py + dotOff, dotSize );
        g2d.fill( innerShape );
    }

    private boolean shouldSkipForLogoPixel( int x, int y, int moduleSize, int imageSize, QRConfig config )
    {
        QRLogoConfig logoConfig = config.getLogo();
        if ( logoConfig == null || logoConfig.getLogoPath() == null || !logoConfig.isRemoveQuietZone() )
        {
            return false;
        }
        // Calculate the area occupied by the logo + padding in pixels
        int logoSize = (int) ( imageSize * logoConfig.getSizeRatio() );
        int logoX = ( imageSize - logoSize ) / 2;
        int logoY = ( imageSize - logoSize ) / 2;
        int padding = logoConfig.isBackgroundEnabled() ? logoConfig.getPadding() : 0;
        int marginPixels = logoConfig.getMarginSize() * moduleSize;
        int skipX = logoX - padding - marginPixels;
        int skipY = logoY - padding - marginPixels;
        int skipSize = logoSize + 2 * ( padding + marginPixels );
        // Module bounds in pixels
        int modXStart = x * moduleSize;
        int modYStart = y * moduleSize;
        int modXEnd = modXStart + moduleSize;
        int modYEnd = modYStart + moduleSize;
        // Check for intersection
        return modXEnd > skipX && modXStart < ( skipX + skipSize ) && modYEnd > skipY
                && modYStart < ( skipY + skipSize );
    }

    private boolean shouldSkipForLogo( int x, int y, int matrixSize, QRConfig config )
    {
        // Legacy method, redirects to new pixel-based logic if possible or uses old logic
        // This is still used by drawBody (double version)
        if ( config.getLogo() != null && config.getLogo().getLogoPath() != null
                && config.getLogo().isRemoveQuietZone() )
        {
            double ratio = config.getLogo().getSizeRatio();
            int centerStart = (int) ( matrixSize * ( 0.5 - ratio / 2 ) );
            int centerEnd = (int) ( matrixSize * ( 0.5 + ratio / 2 ) );
            int margin = config.getLogo().getMarginSize();
            centerStart -= margin;
            centerEnd += margin;
            return x >= centerStart && x <= centerEnd && y >= centerStart && y <= centerEnd;
        }
        return false;
    }

    private Paint getBodyPaint( QRBodyConfig bodyConfig, int size )
    {
        Color c1 = parseColor( bodyConfig.getColor() );
        if ( bodyConfig.getColorDark() != null )
        {
            Color c2 = parseColor( bodyConfig.getColorDark() );
            if ( bodyConfig.isGradientLinear() )
            {
                return new GradientPaint( 0, 0, c1, size, size, c2 );
            }
            else
            {
                return new RadialGradientPaint( new Point2D.Float( size / 2f, size / 2f ), size / 2f, new float[]
                { 0f, 1f }, new Color[]
                { c1, c2 } );
            }
        }
        return c1;
    }

    private void drawModule( Graphics2D g2d, QRBodyShapeEnum shape, int x, int y, double moduleSize, BitMatrix matrix )
    {
        double px = x * moduleSize;
        double py = y * moduleSize;
        float sizeF = (float) moduleSize;
        switch ( shape )
        {
            case SQUARE:
                g2d.fill( new Rectangle2D.Double( px, py, moduleSize, moduleSize ) );
                break;
            case ROUNDED:
                g2d.fill( new RoundRectangle2D.Double( px,
                                                       py,
                                                       moduleSize,
                                                       moduleSize,
                                                       moduleSize * 0.5,
                                                       moduleSize * 0.5 ) );
                break;
            case CIRCLE:
                double pad = moduleSize * 0.15;
                g2d.fill( new Ellipse2D.Double( px + pad, py + pad, moduleSize - 2 * pad, moduleSize - 2 * pad ) );
                break;
            case DOT:
                g2d.fill( new Ellipse2D.Double( px, py, moduleSize, moduleSize ) );
                break;
            case LIQUID:
                drawLiquidModule( g2d, x, y, px, py, sizeF, matrix );
                break;
            default:
                g2d.fill( new Rectangle2D.Double( px, py, moduleSize, moduleSize ) );
                break;
        }
    }

    private void drawLiquidModule( Graphics2D g, int x, int y, double px, double py, float size, BitMatrix matrix )
    {
        boolean nN = y > 0 && matrix.get( x, y - 1 );
        boolean nS = y < matrix.getHeight() - 1 && matrix.get( x, y + 1 );
        boolean nW = x > 0 && matrix.get( x - 1, y );
        boolean nE = x < matrix.getWidth() - 1 && matrix.get( x + 1, y );
        // Corner radii: TopLeft, TopRight, BottomRight, BottomLeft
        float rTL = ( !nN && !nW ) ? size / 2f : 0;
        float rTR = ( !nN && !nE ) ? size / 2f : 0;
        float rBR = ( !nS && !nE ) ? size / 2f : 0;
        float rBL = ( !nS && !nW ) ? size / 2f : 0;
        // If it's a single dot (no neighbors), it becomes a circle
        if ( !nN && !nS && !nW && !nE )
        {
            g.fill( new Ellipse2D.Double( px, py, size, size ) );
            return;
        }
        GeneralPath path = new GeneralPath();
        path.moveTo( px, py + size );
        // TL
        if ( rTL > 0 )
        {
            path.moveTo( px, py + rTL );
            path.quadTo( px, py, px + rTL, py );
        }
        else
        {
            path.moveTo( px, py );
        }
        // Top Edge
        path.lineTo( px + size - rTR, py );
        // TR
        if ( rTR > 0 )
        {
            path.quadTo( px + size, py, px + size, py + rTR );
        }
        else
        {
            path.lineTo( px + size, py );
        }
        // Right Edge
        path.lineTo( px + size, py + size - rBR );
        // BR
        if ( rBR > 0 )
        {
            path.quadTo( px + size, py + size, px + size - rBR, py + size );
        }
        else
        {
            path.lineTo( px + size, py + size );
        }
        // Bottom Edge
        path.lineTo( px + rBL, py + size );
        // BL
        if ( rBL > 0 )
        {
            path.quadTo( px, py + size, px, py + size - rBL );
        }
        else
        {
            path.lineTo( px, py + size );
        }
        path.closePath();
        g.fill( path );
    }

    private void drawEyes( Graphics2D g2d, BitMatrix matrix, QRConfig config, double moduleSize )
    {
        int matrixSize = matrix.getWidth();
        QREyeConfig eyeConfig = config.getEye();
        // Draw three finder patterns
        drawEye( g2d, 0, 0, eyeConfig, moduleSize );
        drawEye( g2d, matrixSize - 7, 0, eyeConfig, moduleSize );
        drawEye( g2d, 0, matrixSize - 7, eyeConfig, moduleSize );
    }

    private void drawEye( Graphics2D g2d, int startX, int startY, QREyeConfig eyeConfig, double moduleSize )
    {
        double px = startX * moduleSize;
        double py = startY * moduleSize;
        double size = 7 * moduleSize;
        Color outerColor = parseColor( eyeConfig.getColorOuter() );
        Color innerColor = parseColor( eyeConfig.getColorInner() );
        QREyeShapeEnum shape = eyeConfig.getShape();
        // Standard QR finder pattern structure (in modules):
        // 7x7 total: 1 black border, 1 white gap, 3x3 black center, 1 white gap, 1 black border
        // So we draw: outer frame (7x7 with 5x5 hole), then inner dot (3x3 at offset 2,2)
        // The 5x5 hole leaves 1-module black walls, the dot is at center
        // Outer Frame - 7x7 outer, 5x5 inner hole (creates 1-module black ring)
        g2d.setColor( outerColor );
        Shape outerShape = createEyeFrame( shape, px, py, size, moduleSize );
        g2d.fill( outerShape );
        // Inner Dot (3x3 at center) - offset by 2 modules from corner
        g2d.setColor( innerColor );
        double dotDim = 3 * moduleSize;
        double dotOff = 2 * moduleSize;
        Shape innerShape = createEyeDot( shape, px + dotOff, py + dotOff, dotDim );
        g2d.fill( innerShape );
    }

    private Shape createEyeFrame( QREyeShapeEnum shape, double x, double y, double size, double pixelSize )
    {
        double wall = pixelSize;
        Shape outer;
        Shape innerHole;
        switch ( shape )
        {
            case CIRCLE:
                outer = new Ellipse2D.Double( x, y, size, size );
                innerHole = new Ellipse2D.Double( x + wall, y + wall, size - 2 * wall, size - 2 * wall );
                break;
            case ROUNDED:
                outer = new RoundRectangle2D.Double( x, y, size, size, size * 0.25, size * 0.25 );
                innerHole = new RoundRectangle2D.Double( x
                        + wall, y + wall, size - 2 * wall, size - 2 * wall, size * 0.2, size * 0.2 );
                break;
            case LEAF:
                GeneralPath leafOuter = new GeneralPath();
                double r = size * 0.4;
                // TL corner (Round)
                leafOuter.moveTo( x, y + r );
                leafOuter.quadTo( x, y, x + r, y );
                // TR Corner (Sharp)
                leafOuter.lineTo( x + size, y );
                // BR Corner (Round)
                leafOuter.lineTo( x + size, y + size - r );
                leafOuter.quadTo( x + size, y + size, x + size - r, y + size );
                // BL Corner (Sharp)
                leafOuter.lineTo( x, y + size );
                leafOuter.closePath();
                outer = leafOuter;
                // Inner Hole 
                GeneralPath leafInner = new GeneralPath();
                double ix = x + wall;
                double iy = y + wall;
                double is = size - 2 * wall;
                double ir = is * 0.4;
                leafInner.moveTo( ix, iy + ir );
                leafInner.quadTo( ix, iy, ix + ir, iy );
                leafInner.lineTo( ix + is, iy );
                leafInner.lineTo( ix + is, iy + is - ir );
                leafInner.quadTo( ix + is, iy + is, ix + is - ir, iy + is );
                leafInner.lineTo( ix, iy + is );
                leafInner.closePath();
                innerHole = leafInner;
                break;
            case SQUARE:
            default:
                outer = new Rectangle2D.Double( x, y, size, size );
                innerHole = new Rectangle2D.Double( x + wall, y + wall, size - 2 * wall, size - 2 * wall );
                break;
        }
        java.awt.geom.Area area = new java.awt.geom.Area( outer );
        area.subtract( new java.awt.geom.Area( innerHole ) );
        return area;
    }

    private Shape createEyeDot( QREyeShapeEnum shape, double x, double y, double size )
    {
        switch ( shape )
        {
            case CIRCLE:
                return new Ellipse2D.Double( x, y, size, size );
            case ROUNDED:
                return new RoundRectangle2D.Double( x, y, size, size, size * 0.3, size * 0.3 );
            case LEAF:
                GeneralPath leafDot = new GeneralPath();
                double dr = size * 0.4;
                leafDot.moveTo( x, y + dr );
                leafDot.quadTo( x, y, x + dr, y );
                leafDot.lineTo( x + size, y );
                leafDot.lineTo( x + size, y + size - dr );
                leafDot.quadTo( x + size, y + size, x + size - dr, y + size );
                leafDot.lineTo( x, y + size );
                leafDot.closePath();
                return leafDot;
            case SQUARE:
            default:
                return new Rectangle2D.Double( x, y, size, size );
        }
    }

    private BufferedImage loadLogo( QRLogoConfig logoConfig )
    {
        try
        {
            if ( logoConfig.getLogoPath().startsWith( "http" ) )
            {
                return ImageIO.read( new URL( logoConfig.getLogoPath() ) );
            }
            else
            {
                // Try to load from classpath
                java.io.InputStream is = getClass().getResourceAsStream( logoConfig.getLogoPath() );
                if ( is == null && !logoConfig.getLogoPath().startsWith( "/" ) )
                {
                    is = getClass().getResourceAsStream( "/" + logoConfig.getLogoPath() );
                }
                if ( is != null )
                {
                    return ImageIO.read( is );
                }
            }
        }
        catch ( Exception e )
        {
            log.warn( "Could not load logo from path: {}", logoConfig.getLogoPath() );
        }
        return null;
    }

    private void drawLogoImage( Graphics2D g2d, QRConfig config, int imageSize, BufferedImage logo )
    {
        try
        {
            QRLogoConfig logoConfig = config.getLogo();
            int logoSize = (int) ( imageSize * logoConfig.getSizeRatio() );
            int logoX = ( imageSize - logoSize ) / 2;
            int logoY = ( imageSize - logoSize ) / 2;
            // Draw logo background
            if ( logoConfig.isBackgroundEnabled() )
            {
                Color bgColor = parseColor( logoConfig.getBackgroundColor() );
                g2d.setColor( bgColor );
                int padding = logoConfig.getPadding();
                int bgSize = logoSize + 2 * padding;
                int bgX = logoX - padding;
                int bgY = logoY - padding;
                if ( logoConfig.isBackgroundRounded() )
                {
                    int cornerRadius = logoConfig.getBackgroundCornerRadius();
                    g2d.fillRoundRect( bgX, bgY, bgSize, bgSize, cornerRadius, cornerRadius );
                }
                else
                {
                    g2d.fillRect( bgX, bgY, bgSize, bgSize );
                }
            }
            g2d.drawImage( logo, logoX, logoY, logoSize, logoSize, null );
        }
        catch ( Exception e )
        {
            log.error( "Error drawing logo", e );
        }
    }

    private void drawBorder( Graphics2D g2d, QRConfig config, int imageSize )
    {
        // Simple border if needed, generally QRs don't need borders
    }

    private boolean isFinderPattern( int x, int y, int matrixSize )
    {
        return isFinderPattern( x, y, matrixSize, 0 );
    }

    private boolean isFinderPattern( int x, int y, int matrixSize, int margin )
    {
        // Top-left
        if ( x >= margin && x < margin + 7 && y >= margin && y < margin + 7 )
            return true;
        // Top-right
        if ( x >= matrixSize - margin - 7 && x < matrixSize - margin && y >= margin && y < margin + 7 )
            return true;
        // Bottom-left
        if ( x >= margin && x < margin + 7 && y >= matrixSize - margin - 7 && y < matrixSize - margin )
            return true;
        return false;
    }

    private Color parseColor( String colorString )
    {
        try
        {
            if ( colorString == null )
                return Color.BLACK;
            if ( colorString.startsWith( "#" ) )
            {
                return Color.decode( colorString );
            }
            else
            {
                return Color.decode( "#" + colorString );
            }
        }
        catch ( Exception e )
        {
            log.warn( "Invalid color: " + colorString + ", using black instead" );
            return Color.BLACK;
        }
    }
}
