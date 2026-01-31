package io.zaplink.core.entity;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.zaplink.core.common.constants.DatabaseConstants;
import io.zaplink.core.common.enums.CampaignStatus;
import io.zaplink.core.common.enums.CampaignType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Unified Campaign entity representing all types of marketing campaigns within organizations.
 * This entity supports both general marketing campaigns and QR code campaigns through the type discriminator.
 * Campaigns can be assigned to influencers for execution and support various campaign types.
 * 
 * @author Zaplink Team
 * @version 2.0
 * @since 2026-01-31
 */
@Entity @Table(name = DatabaseConstants.TABLE_CAMPAIGNS) @EntityListeners(AuditingEntityListener.class) @Data @Builder @NoArgsConstructor @AllArgsConstructor @Accessors(chain = true)
public class Campaign
{
    /**
     * Primary key for the campaign.
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    id;
    /**
     * Name of the campaign.
     */
    @NotBlank @Size(max = 255) @Column(name = DatabaseConstants.COLUMN_NAME, nullable = false)
    private String  name;
    
    /**
     * Campaign ID for external references (unique identifier for QR campaigns).
     * Optional field - only used for QR code campaigns.
     */
    @Column(name = "campaign_id", unique = true)
    private String campaignId;
    
    /**
     * Description of the campaign.
     */
    @Column(name = DatabaseConstants.COLUMN_DESCRIPTION, columnDefinition = "TEXT")
    private String description;
    
    /**
     * Type of the campaign.
     * Determines the campaign behavior and available features.
     */
    @Builder.Default @NotNull @Enumerated(EnumType.STRING) @Column(name = "campaign_type", nullable = false)
    private CampaignType type = CampaignType.GENERAL;
    
    /**
     * User email for QR campaigns.
     * Optional field - only used for QR code campaigns.
     */
    @Column(name = "user_email")
    private String userEmail;
    
    /**
     * Organization ID this campaign belongs to.
     */
    @NotNull @Column(name = DatabaseConstants.COLUMN_ORGANIZATION_ID, nullable = false)
    private Long    organizationId;
    /**
     * Status of the campaign.
     * Valid values: DRAFT, ACTIVE, PAUSED, COMPLETED, INACTIVE, SCHEDULED
     */
    @Builder.Default @Column(name = DatabaseConstants.COLUMN_STATUS)
    @Enumerated(EnumType.STRING)
    private CampaignStatus status = CampaignStatus.DRAFT;
    /**
     * Start date of the campaign.
     */
    @Column(name = DatabaseConstants.COLUMN_START_DATE)
    private Instant startDate;
    /**
     * End date of the campaign.
     */
    @Column(name = DatabaseConstants.COLUMN_END_DATE)
    private Instant endDate;
    /**
     * User ID who created the campaign.
     */
    @NotNull @Column(name = DatabaseConstants.COLUMN_CREATED_BY, nullable = false)
    private Long    createdBy;
    /**
     * Timestamp when the campaign was created.
     */
    @CreatedDate @Column(name = DatabaseConstants.COLUMN_CREATED_AT, nullable = false, updatable = false)
    private Instant createdAt;
    /**
     * Timestamp when the campaign was last updated.
     */
    @LastModifiedDate @Column(name = DatabaseConstants.COLUMN_UPDATED_AT)
    private Instant updatedAt;
    
    // Utility methods for campaign type checking
    
    /**
     * Checks if this is a QR code campaign.
     * 
     * @return true if the campaign type is QR_CODE
     */
    public boolean isQrCodeCampaign() {
        return CampaignType.QR_CODE.equals(this.type);
    }
    
    /**
     * Checks if this is a general campaign.
     * 
     * @return true if the campaign type is GENERAL
     */
    public boolean isGeneralCampaign() {
        return CampaignType.GENERAL.equals(this.type);
    }
    
    /**
     * Gets the campaign name for display purposes.
     * For QR campaigns, returns the campaignId if name is null.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        if (this.name != null && !this.name.trim().isEmpty()) {
            return this.name;
        }
        if (isQrCodeCampaign() && this.campaignId != null) {
            return this.campaignId;
        }
        return "Unnamed Campaign";
    }
    
    /**
     * Factory method to create a QR code campaign.
     * 
     * @param campaignId the unique campaign ID
     * @param campaignName the campaign name
     * @param userEmail the user email
     * @param description the description
     * @param organizationId the organization ID
     * @return a new QR campaign instance
     */
    public static Campaign createQrCampaign(String campaignId, String campaignName, 
                                         String userEmail, String description, 
                                         Long organizationId) {
        return Campaign.builder()
                .campaignId(campaignId)
                .name(campaignName)
                .userEmail(userEmail)
                .description(description)
                .organizationId(organizationId)
                .type(CampaignType.QR_CODE)
                .status(CampaignStatus.ACTIVE)
                .build();
    }
    
    /**
     * Factory method to create a general campaign.
     * 
     * @param name the campaign name
     * @param description the description
     * @param organizationId the organization ID
     * @param createdBy the user ID who created it
     * @return a new general campaign instance
     */
    public static Campaign createGeneralCampaign(String name, String description, 
                                               Long organizationId, Long createdBy) {
        return Campaign.builder()
                .name(name)
                .description(description)
                .organizationId(organizationId)
                .createdBy(createdBy)
                .type(CampaignType.GENERAL)
                .status(CampaignStatus.DRAFT)
                .build();
    }
}
