package io.zaplink.core.service.campaign.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zaplink.core.common.enums.CampaignStatusEnum;
import io.zaplink.core.entity.QrCampaignEntity;
import io.zaplink.core.repository.QrCampaignRepository;
import io.zaplink.core.service.campaign.CampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignServiceImpl implements CampaignService {
    
    private final QrCampaignRepository campaignRepository;
    
    @Override
    @Transactional
    public QrCampaignEntity createCampaign(String campaignName, String userEmail, String description) {
        String campaignId = generateCampaignId();
        
        QrCampaignEntity campaign = QrCampaignEntity.builder()
                .campaignId(campaignId)
                .campaignName(campaignName)
                .userEmail(userEmail)
                .description(description)
                .status(CampaignStatusEnum.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        campaign = campaignRepository.save(campaign);
        log.info("Created campaign: {} for user: {}", campaignId, userEmail);
        
        return campaign;
    }
    
    @Override
    public Optional<QrCampaignEntity> getCampaign(String campaignId, String userEmail) {
        return campaignRepository.findByCampaignId(campaignId)
                .filter(campaign -> campaign.getUserEmail().equals(userEmail));
    }
    
    @Override
    public List<QrCampaignEntity> getCampaignsByUser(String userEmail) {
        return campaignRepository.findByUserEmail(userEmail);
    }
    
    @Override
    @Transactional
    public Optional<QrCampaignEntity> updateCampaign(String campaignId, String userEmail, String campaignName, String description) {
        Optional<QrCampaignEntity> campaignOpt = campaignRepository.findByCampaignId(campaignId);
        
        if (campaignOpt.isEmpty() || !campaignOpt.get().getUserEmail().equals(userEmail)) {
            return Optional.empty();
        }
        
        QrCampaignEntity campaign = campaignOpt.get();
        campaign.setCampaignName(campaignName);
        campaign.setDescription(description);
        campaign.setUpdatedAt(LocalDateTime.now());
        
        campaign = campaignRepository.save(campaign);
        log.info("Updated campaign: {} by user: {}", campaignId, userEmail);
        
        return Optional.of(campaign);
    }
    
    @Override
    @Transactional
    public boolean deleteCampaign(String campaignId, String userEmail) {
        Optional<QrCampaignEntity> campaignOpt = campaignRepository.findByCampaignId(campaignId);
        
        if (campaignOpt.isEmpty() || !campaignOpt.get().getUserEmail().equals(userEmail)) {
            return false;
        }
        
        campaignRepository.delete(campaignOpt.get());
        log.info("Deleted campaign: {} by user: {}", campaignId, userEmail);
        
        return true;
    }
    
    @Override
    @Transactional
    public boolean toggleCampaignStatus(String campaignId, String userEmail) {
        Optional<QrCampaignEntity> campaignOpt = campaignRepository.findByCampaignId(campaignId);
        
        if (campaignOpt.isEmpty() || !campaignOpt.get().getUserEmail().equals(userEmail)) {
            return false;
        }
        
        QrCampaignEntity campaign = campaignOpt.get();
        
        // Toggle between ACTIVE and INACTIVE
        if (campaign.getStatus() == CampaignStatusEnum.ACTIVE) {
            campaign.setStatus(CampaignStatusEnum.INACTIVE);
        } else {
            campaign.setStatus(CampaignStatusEnum.ACTIVE);
        }
        
        campaign.setUpdatedAt(LocalDateTime.now());
        campaignRepository.save(campaign);
        
        log.info("Toggled status for campaign: {} to {} by user: {}", 
                campaignId, campaign.getStatus(), userEmail);
        
        return true;
    }
    
    private String generateCampaignId() {
        return "campaign-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
