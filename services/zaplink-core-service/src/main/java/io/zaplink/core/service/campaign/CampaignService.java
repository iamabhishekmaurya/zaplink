package io.zaplink.core.service.campaign;

import java.util.List;
import java.util.Optional;

import io.zaplink.core.entity.QrCampaignEntity;

public interface CampaignService {
    
    QrCampaignEntity createCampaign(String campaignName, String userEmail, String description);
    
    Optional<QrCampaignEntity> getCampaign(String campaignId, String userEmail);
    
    List<QrCampaignEntity> getCampaignsByUser(String userEmail);
    
    Optional<QrCampaignEntity> updateCampaign(String campaignId, String userEmail, String campaignName, String description);
    
    boolean deleteCampaign(String campaignId, String userEmail);
    
    boolean toggleCampaignStatus(String campaignId, String userEmail);
}
