package io.zaplink.core.common.constants;

/**
 * QR code generation and processing constants.
 * Contains all QR-related configuration values, character sets, and processing constants.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public final class QrConstants
{
    // Prevent instantiation
    private QrConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // ==================== BASE62 ENCODING ====================
    /** Character set for Base62 encoding (0-9, A-Z, a-z). */
    public static final String  BASE62_CHARSET                 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    // ==================== SNOWFLAKE CONFIGURATION ====================
    public static final long    MAX_MACHINE_ID                 = 1023L;
    public static final long    TOTAL_BASE62_COMBINATIONS      = (long) Math.pow( 62, 8 );
    public static final long    MACHINE_ID_BITS                = 10L;
    public static final long    SEQUENCE_BITS                  = 12L;
    public static final long    MAX_SEQUENCE                   = 4095L;
    // ==================== QR GENERATION ====================
    public static final int     DEFAULT_QR_MARGIN              = 1;
    public static final int     DEFAULT_QR_SIZE                = 0;                                                               // Let ZXing determine optimal size
    public static final String  DEFAULT_QR_FORMAT              = "QR_CODE";
    public static final String  DEFAULT_ERROR_CORRECTION_LEVEL = "H";
    // ==================== URL PATTERNS ====================
    public static final String  API_DYNAMIC_QR_IMAGE_PATH      = "/api/v1/dynamic-qr/{}/image";
    public static final String  REDIRECT_URL_PATTERN           = "/r/{}";
    // ==================== VALIDATION ====================
    public static final int     MAX_QR_NAME_LENGTH             = 255;
    public static final int     MAX_DESTINATION_URL_LENGTH     = 2048;
    public static final int     MAX_AVATAR_URL_LENGTH          = 500;
    public static final int     MAX_BIO_TEXT_LENGTH            = 500;
    public static final int     MAX_USERNAME_LENGTH            = 50;
    public static final int     MIN_USERNAME_LENGTH            = 3;
    // ==================== REGEX PATTERNS ====================
    public static final String  USERNAME_PATTERN               = "^[a-zA-Z0-9_-]+$";
    public static final String  EMAIL_PATTERN                  = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String  PHONE_PATTERN                  = "^[+]?[0-9\\s\\-\\(\\)]+$";
    public static final String  URL_PATTERN                    = "^https?://.*";
    // ==================== DEFAULT VALUES ====================
    public static final boolean DEFAULT_TRACK_ANALYTICS        = true;
    public static final boolean DEFAULT_IS_ACTIVE              = true;
    public static final long    DEFAULT_TOTAL_SCANS            = 0L;
    public static final boolean DEFAULT_IS_TRANSPARENT         = false;
    public static final int     DEFAULT_MARGIN                 = 4;
    public static final String  DEFAULT_BODY_COLOR             = "#000000";
    public static final String  DEFAULT_EYE_COLOR              = "#000000";
    public static final String  DEFAULT_BODY_SHAPE             = "SQUARE";
    public static final String  DEFAULT_EYE_SHAPE              = "SQUARE";
    // ==================== QR SHAPES ====================
    public static final String  SHAPE_SQUARE                   = "SQUARE";
    public static final String  SHAPE_CIRCLE                   = "CIRCLE";
    public static final String  SHAPE_ROUNDED                  = "ROUNDED";
    public static final String  SHAPE_DOT                      = "DOT";
    public static final String  SHAPE_DIAMOND                  = "DIAMOND";
    // ==================== LINK TYPES ====================
    public static final String  LINK_TYPE_LINK                 = "LINK";
    public static final String  LINK_TYPE_SOCIAL               = "SOCIAL";
    public static final String  LINK_TYPE_PRODUCT              = "PRODUCT";
    public static final String  LINK_TYPE_EMAIL                = "EMAIL";
    public static final String  LINK_TYPE_PHONE                = "PHONE";
}
