package io.zaplink.core.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zaplink.core.common.constants.ControllerConstants;
import io.zaplink.core.common.constants.LogConstants;
import io.zaplink.core.common.constants.StatusConstants;
import io.zaplink.core.dto.request.PostReviewRequest;
import io.zaplink.core.dto.request.PostSubmissionRequest;
import io.zaplink.core.dto.response.PostResponse;
import io.zaplink.core.service.WorkflowService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for workflow operations.
 * Provides endpoints for post submission, approval, and rejection workflows.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Slf4j @RestController @RequestMapping(ControllerConstants.WORKFLOW_BASE_PATH) @RequiredArgsConstructor @Validated @Tag(name = ControllerConstants.TAG_WORKFLOW_MANAGEMENT, description = ControllerConstants.TAG_WORKFLOW_MANAGEMENT_DESC)
public class WorkflowController
{
  private final WorkflowService workflowService;
  /**
   * Submits a post for approval in the workflow.
   * 
   * @param request The post submission request
   * @return PostResponse with the submitted post information
   */
  @PostMapping(value = ControllerConstants.WORKFLOW_SUBMIT_PATH, version = ControllerConstants.API_VERSION_1) @Operation(summary = ControllerConstants.WORKFLOW_SUBMIT_POST_SUMMARY, description = ControllerConstants.WORKFLOW_SUBMIT_POST_DESC) @ApiResponses(value =
  { @ApiResponse(responseCode = StatusConstants.STATUS_201_CREATED, description = ControllerConstants.RESPONSE_201_POST_SUBMITTED, content = @Content(schema = @Schema(implementation = PostResponse.class))),
    @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_POST_DATA),
    @ApiResponse(responseCode = StatusConstants.STATUS_403_FORBIDDEN, description = ControllerConstants.RESPONSE_403_AUTHOR_NOT_ACTIVE),
    @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_TEAM_NOT_FOUND) })
  public ResponseEntity<PostResponse> submitPost( @Valid @RequestBody PostSubmissionRequest request )
  {
    log.info( LogConstants.POST_SUBMITTING, request.title() );
    PostResponse response = workflowService.submitPost( request );
    return ResponseEntity.status( HttpStatus.CREATED ).body( response );
  }

  /**
   * Approves or rejects a submitted post.
   * 
   * @param request The post review request
   * @return PostResponse with the reviewed post information
   */
  @PostMapping(value = ControllerConstants.WORKFLOW_APPROVE_REJECT_PATH, version = ControllerConstants.API_VERSION_1) @Operation(summary = ControllerConstants.WORKFLOW_REVIEW_POST_SUMMARY, description = ControllerConstants.WORKFLOW_REVIEW_POST_DESC) @ApiResponses(value =
  { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_POST_REVIEWED, content = @Content(schema = @Schema(implementation = PostResponse.class))),
    @ApiResponse(responseCode = StatusConstants.STATUS_400_BAD_REQUEST, description = ControllerConstants.RESPONSE_400_INVALID_REVIEW_DATA),
    @ApiResponse(responseCode = StatusConstants.STATUS_403_FORBIDDEN, description = ControllerConstants.RESPONSE_403_REVIEWER_NOT_APPROVER),
    @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_POST_NOT_FOUND),
    @ApiResponse(responseCode = StatusConstants.STATUS_409_CONFLICT, description = ControllerConstants.RESPONSE_409_POST_NOT_SUBMITTED) })
  public ResponseEntity<PostResponse> reviewPost( @Valid @RequestBody PostReviewRequest request )
  {
    log.info( LogConstants.POST_REVIEWING_WITH_DECISION, request.postId(), request.decision() );
    PostResponse response = workflowService.reviewPost( request );
    return ResponseEntity.ok( response );
  }

  /**
   * Gets all posts pending approval.
   * 
   * @param organizationId The organization ID (from JWT token)
   * @return List of PostResponse for pending posts
   */
  @GetMapping(value = ControllerConstants.WORKFLOW_PENDING_PATH, version = ControllerConstants.API_VERSION_1) @Operation(summary = ControllerConstants.WORKFLOW_GET_PENDING_SUMMARY, description = ControllerConstants.WORKFLOW_GET_PENDING_DESC) @ApiResponses(value =
  { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_PENDING_POSTS_RETRIEVED, content = @Content(schema = @Schema(implementation = List.class))),
    @ApiResponse(responseCode = StatusConstants.STATUS_403_FORBIDDEN, description = ControllerConstants.RESPONSE_403_INVALID_ORG_ACCESS),
    @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_ORGANIZATION_NOT_FOUND) })
  public ResponseEntity<List<PostResponse>> getPendingPosts( @Parameter(description = ControllerConstants.PARAM_DESC_ORG_ID) @RequestHeader(ControllerConstants.HEADER_ORG_ID) Long organizationId )
  {
    log.info( LogConstants.POST_PENDING_RETRIEVING_FOR_ORG, organizationId );
    List<PostResponse> pendingPosts = workflowService.getPendingPosts( organizationId );
    return ResponseEntity.ok( pendingPosts );
  }

  /**
   * Gets all posts by author.
   * 
   * @param authorId The author ID
   * @return List of PostResponse for the author's posts
   */
  @GetMapping(value = ControllerConstants.WORKFLOW_POSTS_AUTHOR_PATH, version = ControllerConstants.API_VERSION_1) @Operation(summary = ControllerConstants.WORKFLOW_GET_POSTS_BY_AUTHOR_SUMMARY, description = ControllerConstants.WORKFLOW_GET_POSTS_BY_AUTHOR_DESC) @ApiResponses(value =
  { @ApiResponse(responseCode = StatusConstants.STATUS_200_OK, description = ControllerConstants.RESPONSE_200_AUTHOR_POSTS_RETRIEVED, content = @Content(schema = @Schema(implementation = List.class))),
    @ApiResponse(responseCode = StatusConstants.STATUS_404_NOT_FOUND, description = ControllerConstants.RESPONSE_404_AUTHOR_NOT_FOUND) })
  public ResponseEntity<List<PostResponse>> getPostsByAuthor( @Parameter(description = ControllerConstants.PARAM_DESC_AUTHOR_ID) @PathVariable Long authorId )
  {
    log.info( LogConstants.POST_RETRIEVING_FOR_AUTHOR, authorId );
    List<PostResponse> posts = workflowService.getPostsByAuthor( authorId );
    return ResponseEntity.ok( posts );
  }
}
