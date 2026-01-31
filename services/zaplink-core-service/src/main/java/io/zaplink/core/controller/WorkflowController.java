package io.zaplink.core.controller;

import io.zaplink.core.dto.request.PostReviewRequest;
import io.zaplink.core.dto.request.PostSubmissionRequest;
import io.zaplink.core.dto.response.PostResponse;
import io.zaplink.core.service.WorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST controller for workflow operations.
 * Provides endpoints for post submission, approval, and rejection workflows.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Slf4j
@RestController
@RequestMapping("/api/workflow")
@RequiredArgsConstructor
@Validated
@Tag(name = "Workflow Management", description = "APIs for managing post approval workflows")
public class WorkflowController {
    
    private final WorkflowService workflowService;
    
    /**
     * Submits a post for approval in the workflow.
     * 
     * @param request The post submission request
     * @return PostResponse with the submitted post information
     */
    @PostMapping("/submit")
    @Operation(summary = "Submit post for approval", description = "Submits a post for approval in the workflow")
    public ResponseEntity<PostResponse> submitPost(@Valid @RequestBody PostSubmissionRequest request) {
        
        log.info("Submitting post for approval: {}", request.title());
        
        PostResponse response = workflowService.submitPost(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }
    
    /**
     * Approves or rejects a submitted post.
     * 
     * @param request The post review request
     * @return PostResponse with the reviewed post information
     */
    @PostMapping("/approve-reject")
    @Operation(summary = "Approve or reject post", description = "Approves or rejects a submitted post")
    public ResponseEntity<PostResponse> reviewPost(@Valid @RequestBody PostReviewRequest request) {
        
        log.info("Reviewing post: {} with decision: {}", request.postId(), request.decision());
        
        PostResponse response = workflowService.reviewPost(request);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Gets all posts pending approval.
     * 
     * @param organizationId The organization ID (from JWT token)
     * @return List of PostResponse for pending posts
     */
    @GetMapping("/pending")
    @Operation(summary = "Get pending posts", description = "Retrieves all posts pending approval")
    public ResponseEntity<List<PostResponse>> getPendingPosts(
            @Parameter(description = "Organization ID") @RequestHeader("X-Org-Id") Long organizationId) {
        
        log.info("Retrieving pending posts for organization: {}", organizationId);
        
        List<PostResponse> pendingPosts = workflowService.getPendingPosts(organizationId);
        
        return ResponseEntity.ok(pendingPosts);
    }
    
    /**
     * Gets all posts by author.
     * 
     * @param authorId The author ID
     * @return List of PostResponse for the author's posts
     */
    @GetMapping("/posts/author/{authorId}")
    @Operation(summary = "Get posts by author", description = "Retrieves all posts created by a specific author")
    public ResponseEntity<List<PostResponse>> getPostsByAuthor(
            @Parameter(description = "Author ID") @PathVariable Long authorId) {
        
        log.info("Retrieving posts for author: {}", authorId);
        
        List<PostResponse> posts = workflowService.getPostsByAuthor(authorId);
        
        return ResponseEntity.ok(posts);
    }
}
