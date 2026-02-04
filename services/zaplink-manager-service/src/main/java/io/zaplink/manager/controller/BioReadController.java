package io.zaplink.manager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.zaplink.manager.dto.bio.BioLinkResponse;
import io.zaplink.manager.dto.bio.BioPageResponse;
import io.zaplink.manager.service.BioReadService;
import lombok.RequiredArgsConstructor;

@RestController @RequestMapping("/") @RequiredArgsConstructor
public class BioReadController
{
    private final BioReadService bioReadService;
    @GetMapping("/bio-pages/{username}")
    public ResponseEntity<BioPageResponse> getBioPage( @PathVariable String username )
    {
        return ResponseEntity.ok( bioReadService.getBioPageByUsername( username ) );
    }

    @GetMapping("/bio-pages/owner/{ownerId}")
    public ResponseEntity<List<BioPageResponse>> getBioPagesByOwner( @PathVariable String ownerId )
    {
        return ResponseEntity.ok( bioReadService.getBioPagesByOwner( ownerId ) );
    }

    @GetMapping("/bio-links/{id}")
    public ResponseEntity<BioLinkResponse> getBioLinkById( @PathVariable Long id )
    {
        return ResponseEntity.ok( bioReadService.getBioLinkById( id ) );
    }

    @GetMapping("/bio-links/page/{pageId}")
    public ResponseEntity<List<BioLinkResponse>> getBioLinksByPageId( @PathVariable Long pageId )
    {
        return ResponseEntity.ok( bioReadService.getBioLinksByPageId( pageId ) );
    }

    @GetMapping("/bio-links/page/{pageId}/active")
    public ResponseEntity<List<BioLinkResponse>> getActiveBioLinksByPageId( @PathVariable Long pageId )
    {
        return ResponseEntity.ok( bioReadService.getActiveBioLinksByPageId( pageId ) );
    }
}
