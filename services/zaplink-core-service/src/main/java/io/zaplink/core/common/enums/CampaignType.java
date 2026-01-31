package io.zaplink.core.common.enums;

/**
 * Enum for campaign types within the Zaplink platform.
 * Defines the different types of campaigns that can be created.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public enum CampaignType {
    
    /**
     * General marketing campaign.
     * Standard marketing campaigns for various purposes.
     */
    GENERAL("General marketing campaign"),
    
    /**
     * QR code campaign.
     * Campaigns specifically designed for QR code generation and tracking.
     */
    QR_CODE("QR code campaign"),
    
    /**
     * Social media campaign.
     * Campaigns focused on social media platforms.
     */
    SOCIAL_MEDIA("Social media campaign"),
    
    /**
     * Email marketing campaign.
     * Campaigns for email marketing purposes.
     */
    EMAIL_MARKETING("Email marketing campaign"),
    
    /**
     * Influencer campaign.
     * Campaigns managed through influencer partnerships.
     */
    INFLUENCER("Influencer campaign"),
    
    /**
     * Video campaign.
     * Campaigns focused on video content.
     */
    VIDEO("Video campaign"),
    
    /**
     * Audio campaign.
     * Campaigns focused on audio content.
     */
    AUDIO("Audio campaign"),
    
    /**
     * Text campaign.
     * Campaigns focused on text-based content.
     */
    TEXT("Text campaign"),
    
    /**
     * Image campaign.
     * Campaigns focused on image-based content.
     */
    IMAGE("Image campaign"),
    
    /**
     * Other campaign.
     * Campaigns that don't fit into other categories.
     */
    OTHER("Other campaign");
    
    private final String description;
    
    CampaignType(String description) {
        this.description = description;
    }
    
    /**
     * Gets the description of this campaign type.
     * 
     * @return Human-readable description of the campaign type
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Checks if this is a QR code campaign.
     * 
     * @return true if the campaign type is QR_CODE
     */
    public boolean isQrCode() {
        return this == QR_CODE;
    }
    
    /**
     * Checks if this is a general campaign.
     * 
     * @return true if the campaign type is GENERAL
     */
    public boolean isGeneral() {
        return this == GENERAL;
    }
    
    /**
     * Checks if this is a social media campaign.
     * 
     * @return true if the campaign type is SOCIAL_MEDIA
     */
    public boolean isSocialMedia() {
        return this == SOCIAL_MEDIA;
    }
    
    /**
     * Checks if this is an email marketing campaign.
     * 
     * @return true if the campaign type is EMAIL_MARKETING
     */
    public boolean isEmailMarketing() {
        return this == EMAIL_MARKETING;
    }
    
    /**
     * Checks if this is an influencer campaign.
     * 
     * @return true if the campaign type is INFLUENCER
     */
    public boolean isInfluencer() {
        return this == INFLUENCER;
    }
    
    /**
     * Checks if this is a video campaign.
     * 
     * @return true if the campaign type is VIDEO
     */
    public boolean isVideo() {
        return this == VIDEO;
    }
    
    /**
     * Checks if this is an audio campaign.
     * 
     * @return true if the campaign type is AUDIO
     */
    public boolean isAudio() {
        return this == AUDIO;
    }
    
    /**
     * Checks if this is a text campaign.
     * 
     * @return true if the campaign type is TEXT
     */
    public boolean isText() {
        return this == TEXT;
    }
    
    /**
     * Checks if this is an image campaign.
     * 
     * @return true if the campaign type is IMAGE
     */
    public boolean isImage() {
        return this == IMAGE;
    }
    
    /**
     * Checks if this is an other campaign.
     * 
     * @return true if the campaign type is OTHER
     */
    public boolean isOther() {
        return this == OTHER;
    }
    
    /**
     * Finds a campaign type by its name (case-insensitive).
     * 
     * @param typeName The campaign type name to search for
     * @return The matching CampaignType, or null if not found
     */
    public static CampaignType fromName(String typeName) {
        if (typeName == null) {
            return null;
        }
        
        try {
            return CampaignType.valueOf(typeName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    /**
     * Gets all campaign types that support QR code functionality.
     * 
     * @return Array of QR code compatible campaign types
     */
    public static CampaignType[] getQrCodeCompatibleTypes() {
        return new CampaignType[]{QR_CODE, GENERAL, INFLUENCER, VIDEO, AUDIO, TEXT, IMAGE, OTHER};
    }
    
    /**
     * Gets all campaign types that support social media integration.
     * 
     * @return Array of social media compatible campaign types
     */
    public static CampaignType[] getSocialMediaCompatibleTypes() {
        return new CampaignType[]{SOCIAL_MEDIA, GENERAL, INFLUENCER, VIDEO, IMAGE, TEXT};
    }
    
    /**
     * Gets all content-based campaign types.
     * 
     * @return Array of content-based campaign types
     */
    public static CampaignType[] getContentBasedTypes() {
        return new CampaignType[]{VIDEO, AUDIO, TEXT, IMAGE};
    }
    
    /**
     * Gets all marketing campaign types.
     * 
     * @return Array of marketing campaign types
     */
    public static CampaignType[] getMarketingTypes() {
        return new CampaignType[]{GENERAL, EMAIL_MARKETING, SOCIAL_MEDIA, INFLUENCER};
    }
}
