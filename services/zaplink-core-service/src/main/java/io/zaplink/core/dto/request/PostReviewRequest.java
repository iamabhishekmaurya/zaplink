package io.zaplink.core.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.zaplink.core.common.constants.ErrorConstant;
import io.zaplink.core.common.constants.MessageConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for approving or rejecting a submitted post.
 * Contains the decision and optional comments.
 * 
 * This is an immutable record representing a post review request.
 * Records provide automatic validation support and are ideal for request DTOs.
 * 
 * @author Zaplink Team
 * @version 1.0
 * @since 2026-01-31
 */
public record PostReviewRequest( @JsonProperty("post_id") @NotNull(message = ErrorConstant.VALIDATION_POST_ID_REQUIRED) Long postId,
                                 @NotBlank(message = ErrorConstant.VALIDATION_DECISION_REQUIRED) String decision,
                                 String comments,
                                 @JsonProperty("reviewer_id") @NotNull(message = ErrorConstant.VALIDATION_REVIEWER_ID_REQUIRED) Long reviewerId )
{
    /**
     * Compact constructor for validation.
     * Validates decision and business rules.
     */
    public PostReviewRequest
    {
        // Validate decision value
        if ( decision != null )
        {
            boolean validDecision = decision.equals( MessageConstants.DECISION_APPROVE )
                    || decision.equals( MessageConstants.DECISION_REJECT );
            if ( !validDecision )
            {
                throw new IllegalArgumentException( ErrorConstant.ERROR_DECISION_MUST_BE_APPROVE_OR_REJECT );
            }
        }
        // Validate post ID
        if ( postId != null && postId <= 0 )
        {
            throw new IllegalArgumentException( ErrorConstant.ERROR_POST_ID_MUST_BE_POSITIVE );
        }
        // Validate reviewer ID
        if ( reviewerId != null && reviewerId <= 0 )
        {
            throw new IllegalArgumentException( ErrorConstant.ERROR_REVIEWER_ID_MUST_BE_POSITIVE );
        }
        // Validate comments length
        if ( comments != null && comments.length() > 1000 )
        {
            throw new IllegalArgumentException( ErrorConstant.ERROR_COMMENTS_CANNOT_EXCEED_1000_CHARACTERS );
        }
    }

    /**
     * Factory method for creating an approval request.
     * 
     * @param postId Post ID
     * @param reviewerId Reviewer ID
     * @return PostReviewRequest instance
     */
    public static PostReviewRequest approve( Long postId, Long reviewerId )
    {
        return new PostReviewRequest( postId, MessageConstants.DECISION_APPROVE, null, reviewerId );
    }

    /**
     * Factory method for creating an approval request with comments.
     * 
     * @param postId Post ID
     * @param comments Review comments
     * @param reviewerId Reviewer ID
     * @return PostReviewRequest instance
     */
    public static PostReviewRequest approveWithComments( Long postId, String comments, Long reviewerId )
    {
        return new PostReviewRequest( postId, MessageConstants.DECISION_APPROVE, comments, reviewerId );
    }

    /**
     * Factory method for creating a rejection request.
     * 
     * @param postId Post ID
     * @param reviewerId Reviewer ID
     * @return PostReviewRequest instance
     */
    public static PostReviewRequest reject( Long postId, Long reviewerId )
    {
        return new PostReviewRequest( postId, MessageConstants.DECISION_REJECT, null, reviewerId );
    }

    /**
     * Factory method for creating a rejection request with comments.
     * 
     * @param postId Post ID
     * @param comments Review comments
     * @param reviewerId Reviewer ID
     * @return PostReviewRequest instance
     */
    public static PostReviewRequest rejectWithComments( Long postId, String comments, Long reviewerId )
    {
        return new PostReviewRequest( postId, MessageConstants.DECISION_REJECT, comments, reviewerId );
    }

    /**
     * Checks if this is an approval request.
     * 
     * @return true if decision is APPROVE
     */
    public boolean isApproval()
    {
        return MessageConstants.DECISION_APPROVE.equals( decision );
    }

    /**
     * Checks if this is a rejection request.
     * 
     * @return true if decision is REJECT
     */
    public boolean isRejection()
    {
        return MessageConstants.DECISION_REJECT.equals( decision );
    }

    /**
     * Checks if the review has comments.
     * 
     * @return true if comments are not null and not empty
     */
    public boolean hasComments()
    {
        return comments != null && !comments.trim().isEmpty();
    }

    /**
     * Gets the decision description.
     * 
     * @return Human-readable decision description
     */
    public String getDecisionDescription()
    {
        return isApproval() ? MessageConstants.DECISION_DESCRIPTION_APPROVED
                            : MessageConstants.DECISION_DESCRIPTION_REJECTED;
    }
}
