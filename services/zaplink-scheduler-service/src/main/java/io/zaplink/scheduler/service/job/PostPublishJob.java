package io.zaplink.scheduler.service.job;

import io.zaplink.scheduler.common.client.SocialClient;
import io.zaplink.scheduler.entity.ScheduledPost;
import io.zaplink.scheduler.repository.ScheduledPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component @Slf4j @RequiredArgsConstructor
public class PostPublishJob
    implements
    Job
{
    private final ScheduledPostRepository scheduledPostRepository;
    private final SocialClient            socialClient;
    @Override @Transactional
    public void execute( JobExecutionContext context )
        throws JobExecutionException
    {
        String postIdStr = context.getJobDetail().getJobDataMap().getString( "postId" );
        log.info( "Executing PostPublishJob for postId: {}", postIdStr );
        UUID postId = UUID.fromString( postIdStr );
        ScheduledPost post = scheduledPostRepository.findById( postId )
                .orElseThrow( () -> new JobExecutionException( "Post not found: " + postId ) );
        if ( post.getStatus() != ScheduledPost.PostStatus.SCHEDULED )
        {
            log.warn( "Post {} is not in SCHEDULED state (current: {}), skipping.", postId, post.getStatus() );
            return;
        }
        try
        {
            // In a real scenario, we would iterate over socialAccountIds and publish to each.
            // For this MVP, we'll assume the social service handles the broadcasting or we call it once.
            // Actually, the requirements check "call zaplink-social-service client directly".
            // Assuming we pass the first account or iterate. Let's iterate.
            for ( UUID socialAccountId : post.getSocialAccountIds() )
            {
                // Mock media URL for now since we haven't integrated Media Service fully to get URL
                // In real impl, we'd fetch asset details.
                String mockMediaUrl = "https://mock-media.com/"
                        + ( post.getMediaAssetIds().isEmpty() ? "default" : post.getMediaAssetIds().get( 0 ) );
                socialClient.publishPost( new SocialClient.PublishRequest( socialAccountId,
                                                                           post.getCaption(),
                                                                           mockMediaUrl ) );
            }
            post.setStatus( ScheduledPost.PostStatus.PUBLISHED );
            scheduledPostRepository.save( post );
            log.info( "Successfully published post {}", postId );
        }
        catch ( Exception e )
        {
            log.error( "Failed to publish post {}", postId, e );
            post.setStatus( ScheduledPost.PostStatus.FAILED );
            scheduledPostRepository.save( post );
            // Optionally re-throw to let Quartz handle retries
        }
    }
}
