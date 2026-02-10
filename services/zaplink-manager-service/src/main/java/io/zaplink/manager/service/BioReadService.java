package io.zaplink.manager.service;

import io.zaplink.manager.dto.bio.BioLinkResponse;
import io.zaplink.manager.dto.bio.BioPageResponse;
import io.zaplink.manager.entity.BioLinkEntity;
import io.zaplink.manager.entity.BioPageEntity;
import io.zaplink.manager.repository.BioPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class BioReadService
{
    private final BioPageRepository                               bioPageRepository;
    private final io.zaplink.manager.repository.BioLinkRepository bioLinkRepository;
    @Transactional(readOnly = true)
    public BioPageResponse getBioPageByUsername( String username )
    {
        BioPageEntity page = bioPageRepository.findByUsernameWithLinks( username )
                .orElseThrow( () -> new IllegalArgumentException( "BioPage not found" ) );
        return mapToPageResponse( page );
    }

    @Transactional(readOnly = true)
    public BioPageResponse getBioPageById( Long id )
    {
        BioPageEntity page = bioPageRepository.findById( id )
                .orElseThrow( () -> new IllegalArgumentException( "BioPage not found" ) );
        return mapToPageResponse( page );
    }

    @Transactional(readOnly = true)
    public List<BioPageResponse> getBioPagesByOwner( String ownerId )
    {
        return bioPageRepository.findByOwnerId( ownerId ).stream().map( this::mapToPageResponse )
                .collect( Collectors.toList() );
    }

    @Transactional(readOnly = true)
    public BioLinkResponse getBioLinkById( Long id )
    {
        BioLinkEntity link = bioLinkRepository.findById( id )
                .orElseThrow( () -> new IllegalArgumentException( "BioLink not found" ) );
        return mapToLinkResponse( link );
    }

    @Transactional(readOnly = true)
    public List<BioLinkResponse> getBioLinksByPageId( Long pageId )
    {
        return bioLinkRepository.findByBioPageIdOrderBySortOrderAsc( pageId ).stream().map( this::mapToLinkResponse )
                .collect( Collectors.toList() );
    }

    @Transactional(readOnly = true)
    public List<BioLinkResponse> getActiveBioLinksByPageId( Long pageId )
    {
        return bioLinkRepository.findByBioPageIdAndIsActiveOrderBySortOrderAsc( pageId, true ).stream()
                .map( this::mapToLinkResponse ).collect( Collectors.toList() );
    }

    private BioPageResponse mapToPageResponse( BioPageEntity page )
    {
        List<BioLinkResponse> links = page.getBioLinks().stream().filter( BioLinkEntity::getIsActive ) // Public view only active? Or maybe all for owner?
                // Let's assume this is public view or standard retrieval.
                // If we need editing view, we might sort/filter differently.
                // Assuming links are fetched already sorted by DB or need sorting here.
                // Entity doesn't guarantee order unless specified.
                .sorted( ( a, b ) -> Integer.compare( a.getSortOrder(), b.getSortOrder() ) )
                .map( this::mapToLinkResponse ).collect( Collectors.toList() );
        return BioPageResponse.builder().id( page.getId() ).username( page.getUsername() ).ownerId( page.getOwnerId() )
                .themeConfig( page.getThemeConfig() ).avatarUrl( page.getAvatarUrl() ).bioText( page.getBioText() )
                .title( page.getTitle() ).coverUrl( page.getCoverUrl() ).seoMeta( page.getSeoMeta() )
                .isPublic( page.getIsPublic() ).createdAt( page.getCreatedAt() ).updatedAt( page.getUpdatedAt() )
                .bioLinks( links ).build();
    }

    private BioLinkResponse mapToLinkResponse( BioLinkEntity link )
    {
        return BioLinkResponse.builder().id( link.getId() ).pageId( link.getBioPage().getId() ).title( link.getTitle() )
                .url( link.getUrl() ).type( link.getTypeName() ).isActive( link.getIsActive() )
                .sortOrder( link.getSortOrder() ).price( link.getPrice() ).currency( link.getCurrency() )
                .metadata( link.getMetadata() ).scheduleFrom( link.getScheduleFrom() )
                .scheduleTo( link.getScheduleTo() ).iconUrl( link.getIconUrl() ).thumbnailUrl( link.getThumbnailUrl() )
                .createdAt( link.getCreatedAt() ).updatedAt( link.getUpdatedAt() ).build();
    }
}
