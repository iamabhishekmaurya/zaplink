package io.zaplink.media.common.constants;

/**
 * Constants for exception messages and error responses.
 */
public final class ExceptionConstants
{
    private ExceptionConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    public static final String ERR_STORAGE_UPLOAD_FAILED = "Failed to upload file to storage";
    public static final String ERR_ASSET_NOT_FOUND       = "Asset not found with id: ";
    public static final String ERR_S3_UPLOAD_FAILED      = "S3 Upload failed";
    public static final String ERR_VALIDATION_FAILED     = "Validation failed";
    public static final String ERR_INTERNAL_SERVER_ERROR = "Internal server error";
    // Prometheus/Text formats
    public static final String ERR_PREFIX_VALIDATION     = "# VALIDATION ERROR: ";
    public static final String ERR_PREFIX_ERROR          = "# ERROR: ";
    public static final String ERR_MISSING_HEADER        = "Missing or Invalid ";
}
