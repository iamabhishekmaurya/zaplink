package io.zaplink.core.entity;

import java.time.LocalDateTime;

import io.zaplink.core.common.enums.CampaignStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "qr_campaigns")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrCampaignEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(name = "campaign_id", nullable = false, unique = true)
    private String campaignId;
    
    @Column(name = "campaign_name", nullable = false)
    private String campaignName;
    
    @Column(name = "user_email", nullable = false)
    private String userEmail;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "start_date")
    private LocalDateTime startDate;
    
    @Column(name = "end_date")
    private LocalDateTime endDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Default
    private CampaignStatusEnum status = CampaignStatusEnum.ACTIVE;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
