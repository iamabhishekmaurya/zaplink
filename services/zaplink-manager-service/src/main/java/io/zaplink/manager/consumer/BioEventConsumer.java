package io.zaplink.manager.consumer;

import io.zaplink.manager.dto.bio.BioLinkResponse;
import io.zaplink.manager.dto.bio.BioPageResponse;
import io.zaplink.manager.dto.event.BioLinkEvent;
import io.zaplink.manager.dto.event.BioPageEvent;
import io.zaplink.manager.entity.BioLinkEntity;
import io.zaplink.manager.entity.BioPageEntity;
import io.zaplink.manager.repository.BioLinkRepository;
import io.zaplink.manager.repository.BioPageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j @Component @RequiredArgsConstructor
public class BioEventConsumer
{
    private final BioPageRepository bioPageRepository;
    private final BioLinkRepository bioLinkRepository;
    @KafkaListener(topics = "bio-events", groupId = "manager-group") @Transactional
    public void consumeBioEvent( Object event )
    {
        log.info( "Received bio event: {}", event );
        if ( event instanceof BioPageEvent pageEvent )
        {
            handleBioPageEvent( pageEvent );
        }
        else if ( event instanceof BioLinkEvent linkEvent )
        {
            handleBioLinkEvent( linkEvent );
        }
        else
        {
            // It might come as a LinkedHashMap if deserialization is generic
            // For now assuming Type Mapping is configured correctly in ConsumerFactory
            log.warn( "Unknown event type: {}", event.getClass() );
        }
    }

    private void handleBioPageEvent( BioPageEvent event )
    {
        BioPageResponse pageDto = event.bioPage();
        log.info( "Processing BioPageEvent: {} for page id: {}", event.eventType(), pageDto.getId() );
        switch ( event.eventType() )
        {
            case "bio.page-created":
            case "bio.page-updated":
            case "bio.links-reordered": // Reordering might affect page update time or structure
                BioPageEntity page = bioPageRepository.findById( pageDto.getId() ).orElse( new BioPageEntity() );
                page.setId( pageDto.getId() );
                page.setUsername( pageDto.getUsername() );
                page.setOwnerId( pageDto.getOwnerId() );
                page.setThemeConfig( pageDto.getThemeConfig() );
                page.setAvatarUrl( pageDto.getAvatarUrl() );
                page.setBioText( pageDto.getBioText() );
                page.setCreatedAt( pageDto.getCreatedAt() );
                page.setUpdatedAt( pageDto.getUpdatedAt() );
                bioPageRepository.save( page );
                break;
            case "bio.page-deleted":
                bioPageRepository.deleteById( pageDto.getId() );
                break;
            default:
                log.warn( "Unknown BioPage event type: {}", event.eventType() );
        }
    }

    private void handleBioLinkEvent( BioLinkEvent event )
    {
        BioLinkResponse linkDto = event.bioLink();
        log.info( "Processing BioLinkEvent: {} for link id: {}", event.eventType(), linkDto.getId() );
        switch ( event.eventType() )
        {
            case "bio.link-added":
            case "bio.link-updated":
                BioPageEntity page = bioPageRepository.findById( linkDto.getPageId() )
                        .orElseThrow( () -> new RuntimeException( "Page not found for link event" ) );
                BioLinkEntity link = bioLinkRepository.findById( linkDto.getId() ).orElse( new BioLinkEntity() );
                link.setId( linkDto.getId() );
                link.setBioPage( page );
                link.setTitle( linkDto.getTitle() );
                link.setUrl( linkDto.getUrl() );
                link.setTypeFromString( linkDto.getType() );
                link.setIsActive( linkDto.isActive() );
                link.setSortOrder( linkDto.getSortOrder() );
                link.setPrice( linkDto.getPrice() );
                link.setCurrency( linkDto.getCurrency() );
                link.setCreatedAt( linkDto.getCreatedAt() );
                link.setUpdatedAt( linkDto.getUpdatedAt() );
                bioLinkRepository.save( link );
                break;
            case "bio.link-deleted":
                bioLinkRepository.deleteById( linkDto.getId() );
                break;
            default:
                log.warn( "Unknown BioLink event type: {}", event.eventType() );
        }
    }
}
