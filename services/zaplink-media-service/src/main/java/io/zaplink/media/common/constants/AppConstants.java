package io.zaplink.media.common.constants;

/**
 * General application constants.
 */
public final class AppConstants
{
    private AppConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // Kafka
    public static final String KAFKA_TOPIC_MEDIA_EVENTS = "media-events";
    public static final String KAFKA_HEADER_TYPE_ID     = "__TypeId__";
    public static final String SERIALIZATION_ID_MEDIA   = "mediaUploaded";
    // Media Types & Prefixes
    public static final String PREFIX_IMAGE             = "image/";
    public static final String PREFIX_THUMBNAILS        = "thumbnails/";
    public static final String EXT_JPG                  = "jpg";
    public static final String MIME_TYPE_JPEG           = "image/jpeg";
    // Headers & API
    public static final String HEADER_API_VERSION       = "X-API-VERSION=1";
    public static final String BASE_PATH_MEDIA          = "/media";
    public static final String BASE_PATH_FOLDERS        = "/folders";
    // Actuator
    public static final String PATH_ACTUATOR            = "/actuator";
    public static final String PATH_HEALTH              = "/health";
}
