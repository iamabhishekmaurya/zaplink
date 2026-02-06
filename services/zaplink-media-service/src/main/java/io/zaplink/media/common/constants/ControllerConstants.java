package io.zaplink.media.common.constants;

/**
 * Controller-related constants for API paths, Swagger documentation, and default values.
 * Contains all controller-specific constants used throughout the Media Service REST API layer.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-02-06
 */
public final class ControllerConstants
{
    private ControllerConstants()
    {
        throw new UnsupportedOperationException( "Utility class cannot be instantiated" );
    }
    // ==================== SWAGGER TAG CONSTANTS ====================
    public static final String TAG_MEDIA_MANAGEMENT          = "Media Management";
    public static final String TAG_MEDIA_MANAGEMENT_DESC     = "APIs for uploading, listing, and managing media assets";
    public static final String TAG_FOLDER_MANAGEMENT         = "Folder Management";
    public static final String TAG_FOLDER_MANAGEMENT_DESC    = "APIs for creating, listing, and managing folders";
    // ==================== MEDIA CONTROLLER OPERATIONS ====================
    public static final String MEDIA_UPLOAD_SUMMARY          = "Upload media file";
    public static final String MEDIA_UPLOAD_DESC             = "Uploads a media file to storage and creates an asset record";
    public static final String MEDIA_LIST_SUMMARY            = "List assets";
    public static final String MEDIA_LIST_DESC               = "Lists assets with optional filtering by folder and owner, with pagination support";
    public static final String MEDIA_DELETE_SUMMARY          = "Delete asset";
    public static final String MEDIA_DELETE_DESC             = "Permanently deletes an asset from storage and database";
    // ==================== FOLDER CONTROLLER OPERATIONS ====================
    public static final String FOLDER_CREATE_SUMMARY         = "Create folder";
    public static final String FOLDER_CREATE_DESC            = "Creates a new folder with optional parent folder";
    public static final String FOLDER_LIST_SUMMARY           = "List folders";
    public static final String FOLDER_LIST_DESC              = "Lists folders for an owner with optional parent filter";
    public static final String FOLDER_DELETE_SUMMARY         = "Delete folder";
    public static final String FOLDER_DELETE_DESC            = "Permanently deletes a folder and all its contents";
    // ==================== SWAGGER RESPONSE CONSTANTS ====================
    // Success Response Descriptions
    public static final String RESPONSE_200_ASSET_UPLOADED   = "Asset uploaded successfully";
    public static final String RESPONSE_200_ASSETS_LISTED    = "Assets retrieved successfully";
    public static final String RESPONSE_204_ASSET_DELETED    = "Asset deleted successfully";
    public static final String RESPONSE_200_FOLDER_CREATED   = "Folder created successfully";
    public static final String RESPONSE_200_FOLDERS_LISTED   = "Folders retrieved successfully";
    public static final String RESPONSE_204_FOLDER_DELETED   = "Folder deleted successfully";
    // Error Response Descriptions
    public static final String RESPONSE_400_INVALID_FILE     = "Invalid file or missing required parameters";
    public static final String RESPONSE_400_INVALID_DATA     = "Invalid input data";
    public static final String RESPONSE_404_ASSET_NOT_FOUND  = "Asset not found";
    public static final String RESPONSE_404_FOLDER_NOT_FOUND = "Folder not found";
    public static final String RESPONSE_500_STORAGE_ERROR    = "Storage operation failed";
    // ==================== SWAGGER PARAMETER CONSTANTS ====================
    public static final String PARAM_FILE                    = "Media file to upload";
    public static final String PARAM_OWNER_ID                = "Owner ID of the user";
    public static final String PARAM_FOLDER_ID               = "Folder ID";
    public static final String PARAM_PARENT_ID               = "Parent folder ID";
    public static final String PARAM_ASSET_ID                = "Asset ID";
}
