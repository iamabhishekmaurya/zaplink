package io.zaplink.manager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.zaplink.manager.dto.BioPageDto;
import io.zaplink.manager.service.BioPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for BioPage read operations (CQRS Query Side).
 * 
 * <p>This controller handles all read operations for bio pages following
 * the CQRS pattern where write operations are handled by the Core Service
 * and read operations are handled by the Manager Service.</p>
 * 
 * <p><strong>Read Operations:</strong></p>
 * <ul>
 *   <li>Get bio page by ID</li>
 *   <li>Get bio page by username</li>
 *   <li>Get bio pages by owner</li>
 *   <li>Get bio pages with pagination</li>
 * </ul>
 */
@RestController
@RequestMapping("/bio-pages")
@RequiredArgsConstructor
@Slf4j
public class BioPageController {
    
    private final BioPageService bioPageService;
    
    @GetMapping("/{id}")
    public ResponseEntity<BioPageDto> getBioPageById(@PathVariable Long id) {
        return bioPageService.getBioPageById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/username/{username}")
    public ResponseEntity<BioPageDto> getBioPageByUsername(@PathVariable String username) {
        return bioPageService.getBioPageByUsername(username)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<BioPageDto>> getBioPagesByOwnerId(@PathVariable String ownerId) {
        List<BioPageDto> pages = bioPageService.getBioPagesByOwnerId(ownerId);
        return ResponseEntity.ok(pages);
    }
    
    @GetMapping
    public ResponseEntity<List<BioPageDto>> getAllBioPages() {
        List<BioPageDto> pages = bioPageService.getBioPagesByOwnerId(null);
        return ResponseEntity.ok(pages);
    }
}
