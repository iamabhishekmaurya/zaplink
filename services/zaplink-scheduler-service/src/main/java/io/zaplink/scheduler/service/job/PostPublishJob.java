package io.zaplink.scheduler.service.job;

import java.util.UUID;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.zaplink.scheduler.common.client.SocialClient;
import io.zaplink.scheduler.common.constants.DbIdentifiers;
import io.zaplink.scheduler.common.constants.LogMessages;
import io.zaplink.scheduler.common.enums.PostStatus;
import io.zaplink.scheduler.entity.ScheduledPost;
import io.zaplink.scheduler.repository.ScheduledPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Quartz Job for publishing scheduled posts.
 * <p>
 * This job is triggered by the Quartz Scheduler at the scheduled time.
 * It retrieves the post, validates its status, and publishes it to the associated social accounts
 * via the Social Service.
 * </p>
 */
@Component @Slf4j @RequiredArgsConstructor
public class PostPublishJob
    implements
    Job
{
    private final ScheduledPostRepository scheduledPostRepository;
    private final SocialClient            socialClient;
    /**
     * Executes the post publishing logic.
     *
     * @param context The Quartz job execution context containing job data (e.g., postId).
     * @throws JobExecutionException If the post cannot be found or a critical error occurs.
     */
    @Override @Transactional
    public void execute( JobExecutionContext context )
        throws JobExecutionException
    {
        // 1. Retrieve Post ID from Job Data
        String postIdStr = context.getJobDetail().getJobDataMap().getString( DbIdentifiers.JOB_DATA_POST_ID );
        log.info( LogMessages.EXECUTE_PUBLISH_JOB, postIdStr );
        UUID postId = UUID.fromString( postIdStr );
        // 2. Fetch Post from Database
        ScheduledPost post = scheduledPostRepository.findById( postId )
                .orElseThrow( () -> new JobExecutionException( "Post not found: " + postId ) );
        // 3. Validate Post Status (Idempotency Check)
        // Ensure we don't re-publish if it's already done or cancelled.
        if ( post.getStatus() != PostStatus.SCHEDULED )
        {
            log.warn( LogMessages.POST_NOT_SCHEDULED, postId, post.getStatus() );
            return;
        }
        try
        {
            // 4. Publish to Social Accounts
            // Iterate through all social accounts linked to this post.
            for ( UUID socialAccountId : post.getSocialAccountIds() )
            {
                // TODO: Integrate with Media Service to fetch actual signed URLs for assets.
                // Currently using a mock URL generator for MVP.
                String mockMediaUrl = "https://mock-media.com/"
                        + ( post.getMediaAssetIds().isEmpty() ? "default" : post.getMediaAssetIds().get( 0 ) );
                // Call Social Service Feign Client
                socialClient.publishPost( new SocialClient.PublishRequest( socialAccountId,
                                                                           post.getCaption(),
                                                                           mockMediaUrl ) );
            }
            // 5. Update Status to PUBLISHED
            post.setStatus( PostStatus.PUBLISHED );
            scheduledPostRepository.save( post );
            log.info( LogMessages.PUBLISHED_SUCCESS, postId );
        }
        catch ( Exception e )
        {
            // 6. Handle Failures
            // Log error and update status to FAILED so it can be retried or inspected manualy.
            log.error( LogMessages.ERR_PUBLISH_FAILED, postId, e );
            post.setStatus( PostStatus.FAILED );
            scheduledPostRepository.save( post );
            // We optionally re-throw to let Quartz handle retries if configured,
            // but for now we swallow it after marking as FAILED to prevent loop.
        }
    }
}
