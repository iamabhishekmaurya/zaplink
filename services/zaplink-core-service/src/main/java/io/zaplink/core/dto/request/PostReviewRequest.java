package io.zaplink.core.dto.request;

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
public record PostReviewRequest(
    
    /**
     * Post ID being reviewed.
     */
    @NotNull(message = "Post ID is required")
    Long postId,
    
    /**
     * Review decision (APPROVE or REJECT).
     */
    @NotBlank(message = "Decision is required")
    String decision,
    
    /**
     * Reviewer comments explaining the decision.
     */
    String comments,
    
    /**
     * Reviewer ID.
     */
    @NotNull(message = "Reviewer ID is required")
    Long reviewerId
) {
    
    /**
     * Compact constructor for validation.
     * Validates decision and business rules.
     */
    public PostReviewRequest {
        // Validate decision value
        if (decision != null) {
            boolean validDecision = decision.equals("APPROVE") || decision.equals("REJECT");
            if (!validDecision) {
                throw new IllegalArgumentException("Decision must be either APPROVE or REJECT");
            }
        }
        
        // Validate post ID
        if (postId != null && postId <= 0) {
            throw new IllegalArgumentException("Post ID must be positive");
        }
        
        // Validate reviewer ID
        if (reviewerId != null && reviewerId <= 0) {
            throw new IllegalArgumentException("Reviewer ID must be positive");
        }
        
        // Validate comments length
        if (comments != null && comments.length() > 1000) {
            throw new IllegalArgumentException("Comments cannot exceed 1000 characters");
        }
    }
    
    /**
     * Factory method for creating an approval request.
     * 
     * @param postId Post ID
     * @param reviewerId Reviewer ID
     * @return PostReviewRequest instance
     */
    public static PostReviewRequest approve(
        Long postId,
        Long reviewerId
    ) {
        return new PostReviewRequest(postId, "APPROVE", null, reviewerId);
    }
    
    /**
     * Factory method for creating an approval request with comments.
     * 
     * @param postId Post ID
     * @param comments Review comments
     * @param reviewerId Reviewer ID
     * @return PostReviewRequest instance
     */
    public static PostReviewRequest approveWithComments(
        Long postId,
        String comments,
        Long reviewerId
    ) {
        return new PostReviewRequest(postId, "APPROVE", comments, reviewerId);
    }
    
    /**
     * Factory method for creating a rejection request.
     * 
     * @param postId Post ID
     * @param reviewerId Reviewer ID
     * @return PostReviewRequest instance
     */
    public static PostReviewRequest reject(
        Long postId,
        Long reviewerId
    ) {
        return new PostReviewRequest(postId, "REJECT", null, reviewerId);
    }
    
    /**
     * Factory method for creating a rejection request with comments.
     * 
     * @param postId Post ID
     * @param comments Review comments
     * @param reviewerId Reviewer ID
     * @return PostReviewRequest instance
     */
    public static PostReviewRequest rejectWithComments(
        Long postId,
        String comments,
        Long reviewerId
    ) {
        return new PostReviewRequest(postId, "REJECT", comments, reviewerId);
    }
    
    /**
     * Checks if this is an approval request.
     * 
     * @return true if decision is APPROVE
     */
    public boolean isApproval() {
        return "APPROVE".equals(decision);
    }
    
    /**
     * Checks if this is a rejection request.
     * 
     * @return true if decision is REJECT
     */
    public boolean isRejection() {
        return "REJECT".equals(decision);
    }
    
    /**
     * Checks if the review has comments.
     * 
     * @return true if comments are not null and not empty
     */
    public boolean hasComments() {
        return comments != null && !comments.trim().isEmpty();
    }
    
    /**
     * Gets the decision description.
     * 
     * @return Human-readable decision description
     */
    public String getDecisionDescription() {
        return isApproval() ? "Approved" : "Rejected";
    }
}
