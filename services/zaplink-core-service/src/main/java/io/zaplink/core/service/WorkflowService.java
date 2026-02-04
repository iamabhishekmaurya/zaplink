package io.zaplink.core.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.common.constants.LogConstants;
import io.zaplink.core.common.constants.MessageConstants;
import io.zaplink.core.common.enums.PostStatus;
import io.zaplink.core.dto.event.WorkflowStatusChangedEvent;
import io.zaplink.core.dto.request.PostReviewRequest;
import io.zaplink.core.dto.request.PostSubmissionRequest;
import io.zaplink.core.dto.response.PostResponse;
import io.zaplink.core.entity.Post;
import io.zaplink.core.entity.TeamMember;
import io.zaplink.core.repository.PostRepository;
import io.zaplink.core.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for workflow management operations.
 * Handles post submission, approval, and rejection workflows.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
@Slf4j @Service @RequiredArgsConstructor
public class WorkflowService
{
    private final PostRepository        postRepository;
    private final TeamMemberRepository  teamMemberRepository;
    private final EventPublisherService eventPublisherService;
    /**
     * Submits a post for approval in the workflow.
     * 
     * @param request The post submission request
     * @return PostResponse with the submitted post information
     */
    @Transactional
    public PostResponse submitPost( PostSubmissionRequest request )
    {
        log.info( LogConstants.POST_SUBMITTING, "Title: {}, Author: {}", request.title(), request.authorId() );
        // Validate author is an active team member
        TeamMember authorMember = teamMemberRepository.findByUserId( request.authorId() ).stream()
                .filter( member -> MessageConstants.STATUS_ACTIVE.equals( member.getStatus() ) ).findFirst()
                .orElseThrow( () -> new RuntimeException( ErrorConstant.ERROR_AUTHOR_NOT_ACTIVE_TEAM_MEMBER ) );
        // Create post
        Post post = Post.builder().title( request.title() ).content( request.content() )
                .campaignId( request.campaignId() ).authorId( request.authorId() ).status( PostStatus.SUBMITTED )
                .submittedAt( Instant.now() ).build();
        post = postRepository.save( post );
        // Publish workflow status change event
        WorkflowStatusChangedEvent event = WorkflowStatusChangedEvent
                .create( post.getId(), post.getTitle(), MessageConstants.POST_STATUS_DRAFT,
                         MessageConstants.POST_STATUS_SUBMITTED, request.authorId(),
                         generateUsernameFromUserId( request.authorId() ),
                         generateEmailFromUserId( request.authorId() ), 1L, "" );
        eventPublisherService.publishWorkflowStatusChangedEvent( event );
        log.info( LogConstants.POST_SUBMITTED, "Post submitted for approval successfully: {}", post.getId() );
        return mapToPostResponse( post );
    }

    /**
     * Approves or rejects a submitted post.
     * 
     * @param request The post review request
     * @return PostResponse with the reviewed post information
     */
    @Transactional
    public PostResponse reviewPost( PostReviewRequest request )
    {
        log.info( LogConstants.POST_REVIEWING, "Post: {}, Decision: {}, Reviewer: {}", request.postId(),
                  request.decision(), request.reviewerId() );
        // Validate reviewer is an active team member with approver role
        TeamMember reviewerMember = teamMemberRepository.findByUserId( request.reviewerId() ).stream()
                .filter( member -> MessageConstants.STATUS_ACTIVE.equals( member.getStatus() )
                        && ( MessageConstants.ROLE_APPROVER.equals( member.getRole() )
                                || MessageConstants.ROLE_ADMIN.equals( member.getRole() ) ) )
                .findFirst()
                .orElseThrow( () -> new RuntimeException( ErrorConstant.ERROR_REVIEWER_NOT_ACTIVE_APPROVER ) );
        Post post = postRepository.findById( request.postId() ).orElseThrow( () -> new RuntimeException( String
                .format( ErrorConstant.ERROR_POST_NOT_FOUND, request.postId() ) ) );
        // Validate post is in submitted status
        if ( !PostStatus.SUBMITTED.equals( post.getStatus() ) )
        {
            throw new RuntimeException( ErrorConstant.ERROR_POST_NOT_SUBMITTED_STATUS );
        }
        PostStatus previousStatus = post.getStatus();
        PostStatus newStatus = MessageConstants.DECISION_APPROVE
                .equalsIgnoreCase( request.decision() ) ? PostStatus.APPROVED : PostStatus.REJECTED;
        // Update post
        post.setStatus( newStatus );
        post.setReviewedBy( request.reviewerId() );
        post.setReviewedAt( Instant.now() );
        post.setReviewComments( request.comments() );
        if ( PostStatus.APPROVED.equals( newStatus ) )
        {
            post.setPublishedAt( Instant.now() );
        }
        post = postRepository.save( post );
        // Publish workflow status change event
        WorkflowStatusChangedEvent event = new WorkflowStatusChangedEvent( WorkflowStatusChangedEvent.EVENT_TYPE,
                                                                           null, // eventId will be generated in compact constructor
                                                                           null, // timestamp will be generated in compact constructor
                                                                           post.getId(),
                                                                           post.getTitle(),
                                                                           previousStatus.name(),
                                                                           newStatus.name(),
                                                                           post.getAuthorId(),
                                                                           generateUsernameFromUserId( post
                                                                                   .getAuthorId() ),
                                                                           generateEmailFromUserId( post
                                                                                   .getAuthorId() ),
                                                                           post.getCampaignId(),
                                                                           "", // Would be populated from campaign service
                                                                           reviewerMember.getTeamId(),
                                                                           "", // Would be populated from team service
                                                                           1L, // Would be populated from organization service
                                                                           "", // Would be populated from organization service
                                                                           request.reviewerId(),
                                                                           generateUsernameFromUserId( request
                                                                                   .reviewerId() ),
                                                                           request.comments(),
                                                                           post.getReviewedAt() );
        eventPublisherService.publishWorkflowStatusChangedEvent( event );
        log.info( LogConstants.POST_REVIEWED, "Post reviewed successfully: {}", post.getId() );
        return mapToPostResponse( post );
    }

    /**
     * Gets all posts pending approval.
     * 
     * @param organizationId The organization ID
     * @return List of PostResponse for pending posts
     */
    public List<PostResponse> getPendingPosts( Long organizationId )
    {
        List<Post> pendingPosts = postRepository.findPendingApprovalByOrganization( organizationId );
        return pendingPosts.stream().map( this::mapToPostResponse ).collect( Collectors.toList() );
    }

    /**
     * Gets all posts by author.
     * 
     * @param authorId The author ID
     * @return List of PostResponse for the author's posts
     */
    public List<PostResponse> getPostsByAuthor( Long authorId )
    {
        List<Post> posts = postRepository.findByAuthorId( authorId );
        return posts.stream().map( this::mapToPostResponse ).collect( Collectors.toList() );
    }

    /**
     * Maps Post entity to PostResponse DTO.
     * 
     * @param post The post entity
     * @return PostResponse DTO
     */
    private PostResponse mapToPostResponse( Post post )
    {
        return new PostResponse( post.getId(),
                                 post.getTitle(),
                                 post.getContent(),
                                 post.getCampaignId(),
                                 "", // Would be populated from campaign service
                                 post.getAuthorId(),
                                 generateUsernameFromUserId( post.getAuthorId() ),
                                 generateEmailFromUserId( post.getAuthorId() ),
                                 post.getStatus().name(),
                                 post.getSubmittedAt(),
                                 post.getReviewedBy(),
                                 post.getReviewedBy() != null ? generateUsernameFromUserId( post.getReviewedBy() )
                                                              : null,
                                 post.getReviewedAt(),
                                 post.getReviewComments(),
                                 post.getPublishedAt(),
                                 post.getCreatedAt(),
                                 post.getUpdatedAt() );
    }

    /**
     * Generates a placeholder username from user ID.
     * In a real implementation, this would look up the user in the user service.
     * 
     * @param userId The user ID
     * @return Generated username
     */
    private String generateUsernameFromUserId( Long userId )
    {
        // Placeholder implementation - in reality, you'd call the auth service
        return "user" + userId;
    }

    /**
     * Generates a placeholder email from user ID.
     * In a real implementation, this would look up the user in the user service.
     * 
     * @param userId The user ID
     * @return Generated email
     */
    private String generateEmailFromUserId( Long userId )
    {
        // Placeholder implementation - in reality, you'd call the auth service
        return "user" + userId + "@example.com";
    }
}
