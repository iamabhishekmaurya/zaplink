package io.zaplink.manager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.zaplink.manager.dto.BioLinkDto;
import io.zaplink.manager.service.BioLinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for BioLink read operations (CQRS Query Side).
 * 
 * <p>This controller handles all read operations for bio links following
 * the CQRS pattern where write operations are handled by the Core Service
 * and read operations are handled by the Manager Service.</p>
 * 
 * <p><strong>Read Operations:</strong></p>
 * <ul>
 *   <li>Get bio link by ID</li>
 *   <li>Get bio links by page ID</li>
 *   <li>Get active bio links by page ID</li>
 *   <li>Get all bio links</li>
 * </ul>
 */
@RestController
@RequestMapping("/bio-links")
@RequiredArgsConstructor
@Slf4j
public class BioLinkController {
    
    private final BioLinkService bioLinkService;
    
    @GetMapping("/{id}")
    public ResponseEntity<BioLinkDto> getBioLinkById(@PathVariable Long id) {
        return bioLinkService.getBioLinkById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/page/{pageId}")
    public ResponseEntity<List<BioLinkDto>> getBioLinksByPageId(@PathVariable Long pageId) {
        List<BioLinkDto> links = bioLinkService.getBioLinksByPageId(pageId);
        return ResponseEntity.ok(links);
    }
    
    @GetMapping("/page/{pageId}/active")
    public ResponseEntity<List<BioLinkDto>> getActiveBioLinksByPageId(@PathVariable Long pageId) {
        List<BioLinkDto> links = bioLinkService.getActiveBioLinksByPageId(pageId);
        return ResponseEntity.ok(links);
    }
    
    @GetMapping
    public ResponseEntity<List<BioLinkDto>> getAllBioLinks() {
        List<BioLinkDto> links = bioLinkService.getBioLinksByPageId(null);
        return ResponseEntity.ok(links);
    }
}
