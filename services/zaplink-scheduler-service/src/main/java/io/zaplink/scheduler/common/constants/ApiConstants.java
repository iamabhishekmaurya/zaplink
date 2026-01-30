package io.zaplink.scheduler.common.constants;

/**
 * API Endpoint definitions and HTTP Header constants.
 * <p>
 * Defines the contract for URL paths, header keys, and parameters used by the Scheduler Controller.
 * </p>
 */
public final class ApiConstants
{
    private ApiConstants()
    {
    }
    public static final String BASE_URI            = "/schedule";
    public static final String DRAG_UPDATE_URI     = "/{id}/drag";
    public static final String HEADER_USER_ID      = "X-User-Id";
    // Social Service Client
    public static final String SOCIAL_SERVICE_NAME = "zaplink-social-service";
    public static final String SOCIAL_SERVICE_URL  = "${application.social-service.url:http://localhost:8088}";
    public static final String SOCIAL_PUBLISH_URI  = "/social/publish";
    public static final String PARAM_START         = "start";
    public static final String PARAM_END           = "end";
}
