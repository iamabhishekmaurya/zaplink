package io.zaplink.core.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zaplink.core.common.constants.LogConstants;
import io.zaplink.core.common.enums.CampaignStatus;
import io.zaplink.core.common.enums.CampaignType;
import io.zaplink.core.entity.Campaign;
import io.zaplink.core.repository.QrCampaignRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @RequiredArgsConstructor @Slf4j
public class CampaignService
{
    private final QrCampaignRepository campaignRepository;
    @Transactional
    public Campaign createQrCampaign( String campaignName, String userEmail, String description, Long organizationId )
    {
        String campaignId = generateCampaignId();
        Campaign campaign = Campaign.createQrCampaign( campaignId, campaignName, userEmail, description,
                                                       organizationId );
        campaign = campaignRepository.save( campaign );
        log.info( LogConstants.CAMPAIGN_QR_CREATED, campaignId, userEmail );
        return campaign;
    }

    @Transactional
    public Campaign createGeneralCampaign( String name, String description, Long organizationId, Long createdBy )
    {
        Campaign campaign = Campaign.createGeneralCampaign( name, description, organizationId, createdBy );
        campaign = campaignRepository.save( campaign );
        log.info( LogConstants.CAMPAIGN_GENERAL_CREATED, name, organizationId );
        return campaign;
    }

    public Optional<Campaign> getCampaign( String campaignId, String userEmail )
    {
        return campaignRepository.findByCampaignId( campaignId )
                .filter( campaign -> campaign.getUserEmail() != null && campaign.getUserEmail().equals( userEmail ) );
    }

    public List<Campaign> getCampaignsByUser( String userEmail )
    {
        return campaignRepository.findByUserEmail( userEmail );
    }

    public List<Campaign> getQrCampaignsByUser( String userEmail )
    {
        return campaignRepository.findByTypeAndUserEmail( CampaignType.QR_CODE, userEmail );
    }

    public List<Campaign> getActiveQrCampaignsByUser( String userEmail )
    {
        return campaignRepository.findActiveQrCampaignsByUserEmail( userEmail );
    }

    @Transactional
    public Optional<Campaign> updateCampaign( String campaignId,
                                              String userEmail,
                                              String campaignName,
                                              String description )
    {
        Optional<Campaign> campaignOpt = campaignRepository.findByCampaignId( campaignId );
        if ( campaignOpt.isEmpty() || !campaignOpt.get().getUserEmail().equals( userEmail ) )
        {
            return Optional.empty();
        }
        Campaign campaign = campaignOpt.get();
        campaign.setName( campaignName );
        campaign.setDescription( description );
        campaign = campaignRepository.save( campaign );
        log.info( LogConstants.CAMPAIGN_UPDATED, campaignId, userEmail );
        return Optional.of( campaign );
    }

    @Transactional
    public boolean deleteCampaign( String campaignId, String userEmail )
    {
        Optional<Campaign> campaignOpt = campaignRepository.findByCampaignId( campaignId );
        if ( campaignOpt.isEmpty() || !campaignOpt.get().getUserEmail().equals( userEmail ) )
        {
            return false;
        }
        campaignRepository.delete( campaignOpt.get() );
        log.info( LogConstants.CAMPAIGN_DELETED, campaignId, userEmail );
        return true;
    }

    @Transactional
    public boolean toggleCampaignStatus( String campaignId, String userEmail )
    {
        Optional<Campaign> campaignOpt = campaignRepository.findByCampaignId( campaignId );
        if ( campaignOpt.isEmpty() || !campaignOpt.get().getUserEmail().equals( userEmail ) )
        {
            return false;
        }
        Campaign campaign = campaignOpt.get();
        // Toggle between ACTIVE and INACTIVE
        if ( CampaignStatus.ACTIVE.equals( campaign.getStatus() ) )
        {
            campaign.setStatus( CampaignStatus.INACTIVE );
        }
        else
        {
            campaign.setStatus( CampaignStatus.ACTIVE );
        }
        campaignRepository.save( campaign );
        log.info( LogConstants.CAMPAIGN_STATUS_TOGGLED, campaignId, campaign.getStatus(), userEmail );
        return true;
    }

    private String generateCampaignId()
    {
        return "campaign-" + UUID.randomUUID().toString().substring( 0, 8 );
    }
}
