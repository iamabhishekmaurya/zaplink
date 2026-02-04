package io.zaplink.core.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.zaplink.core.common.constants.ErrorConstant;

/**
 * DTO for submitting a post for approval in the workflow.
 * Contains the post content and campaign information.
 * 
 * This is an immutable record representing a post submission request.
 * Records provide automatic validation support and are ideal for request DTOs.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public record PostSubmissionRequest( @NotBlank(message = ErrorConstant.VALIDATION_TITLE_REQUIRED) String title,
                                     String content,
                                     Long campaignId,
                                     @NotNull(message = ErrorConstant.VALIDATION_AUTHOR_ID_REQUIRED) Long authorId,
                                     String reviewerNotes )
{
    /**
     * Compact constructor for validation.
     * Validates required fields and business rules.
     */
    public PostSubmissionRequest
    {
        // Validate title length
        if ( title != null && title.length() > 200 )
        {
            throw new IllegalArgumentException( "Title cannot exceed 200 characters" );
        }
        // Validate content length
        if ( content != null && content.length() > 10000 )
        {
            throw new IllegalArgumentException( "Content cannot exceed 10,000 characters" );
        }
        // Validate campaign ID if provided
        if ( campaignId != null && campaignId <= 0 )
        {
            throw new IllegalArgumentException( "Campaign ID must be positive" );
        }
        // Validate author ID
        if ( authorId != null && authorId <= 0 )
        {
            throw new IllegalArgumentException( "Author ID must be positive" );
        }
    }

    /**
     * Factory method for creating a post submission without a campaign.
     * 
     * @param title Post title
     * @param content Post content
     * @param authorId Author ID
     * @return PostSubmissionRequest instance
     */
    public static PostSubmissionRequest withoutCampaign( String title, String content, Long authorId )
    {
        return new PostSubmissionRequest( title, content, null, authorId, null );
    }

    /**
     * Factory method for creating a post submission with a campaign.
     * 
     * @param title Post title
     * @param content Post content
     * @param campaignId Campaign ID
     * @param authorId Author ID
     * @return PostSubmissionRequest instance
     */
    public static PostSubmissionRequest withCampaign( String title, String content, Long campaignId, Long authorId )
    {
        return new PostSubmissionRequest( title, content, campaignId, authorId, null );
    }

    /**
     * Checks if this post is part of a campaign.
     * 
     * @return true if campaign ID is not null
     */
    public boolean isPartOfCampaign()
    {
        return campaignId != null;
    }

    /**
     * Checks if the post has reviewer notes.
     * 
     * @return true if reviewer notes are not null and not empty
     */
    public boolean hasReviewerNotes()
    {
        return reviewerNotes != null && !reviewerNotes.trim().isEmpty();
    }

    /**
     * Gets the content preview (first 100 characters).
     * 
     * @return Content preview
     */
    public String getContentPreview()
    {
        if ( content == null || content.isEmpty() )
        {
            return "";
        }
        return content.length() > 100 ? content.substring( 0, 100 ) + "..." : content;
    }

    /**
     * Gets the word count of the content.
     * 
     * @return Word count
     */
    public int getWordCount()
    {
        if ( content == null || content.trim().isEmpty() )
        {
            return 0;
        }
        return content.trim().split( "\\s+" ).length;
    }
}
