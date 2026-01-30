package io.zaplink.social.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.zaplink.social.common.constants.ErrorMessages;
import io.zaplink.social.common.constants.LogMessages;
import io.zaplink.social.common.enums.SocialProvider;
import io.zaplink.social.entity.SocialAccount;
import io.zaplink.social.repository.SocialAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service responsible for managing social media account connections and conducting
 * post publishing operations.
 * <p>
 * This service acts as the bridge between the Zaplink platform and external social
 * media providers (e.g., Instagram, Facebook, LinkedIn). It handles the OAuth2
 * credential lifecycle and the execution of content publishing.
 * </p>
 * <p>
 * <b>Modules:</b>
 * <ul>
 *     <li>Account Management: Connecting and storing credentials.</li>
 *     <li>Publishing: Sending content to external APIs.</li>
 * </ul>
 * </p>
 * <b>Assumptions:</b>
 * <ul>
 *     <li>The 'ownerId' is pre-validated by the Gateway/Auth service.</li>
 *     <li>The internal database is the source of truth for token validity.</li>
 * </ul>
 */
@Service @RequiredArgsConstructor @Slf4j
public class SocialService
{
    private final SocialAccountRepository socialAccountRepository;
    /**
     * Connects a user's social media account by exchanging an auth code for access tokens.
     * 
     * <p><b>Functionality:</b></p>
     * <ol>
     *     <li>Validates the requested provider string.</li>
     *     <li>Exchanges the 'authCode' for an Access Token and Refresh Token (Mocked).</li>
     *     <li>Calculates token expiry.</li>
     *     <li>Persists the new {@link SocialAccount} linked to the owner.</li>
     * </ol>
     *
     * <p><b>TODO:</b></p>
     * <ul>
     *     <li>Integrate with actual Provider APIs (e.g., Graph API) using WebClient.</li>
     *     <li>Implement idempotency: Check if the account is already connected and update tokens instead of creating new.</li>
     *     <li>Encrypt 'accessToken' and 'refreshToken' before storing in the database for security.</li>
     *     <li>Externalize constants to configuration bundles (Suggested Improvement).</li>
     * </ul>
     *
     * @param providerStr The case-insensitive name of the social provider (e.g., "INSTAGRAM").
     * @param authCode    The temporary authorization code from the provider's OAuth callback.
     * @param ownerId     The UUID of the authenticated user.
     * @return The persisted {@link SocialAccount} entity.
     * @throws IllegalArgumentException if the provider is unsupported or invalid.
     */
    public SocialAccount connectAccount( String providerStr, String authCode, UUID ownerId )
    {
        log.info( LogMessages.CONNECT_ENTRY, providerStr, ownerId );
        log.debug( LogMessages.AUTH_CODE_DEBUG, ( authCode != null ? authCode.length() : "null" ) );
        // 1. Validate Provider
        SocialProvider provider;
        try
        {
            provider = SocialProvider.valueOf( providerStr.toUpperCase() );
        }
        catch ( IllegalArgumentException | NullPointerException e )
        {
            log.error( LogMessages.CONNECT_VALIDATION_FAILED, providerStr, e );
            throw new IllegalArgumentException( ErrorMessages.INVALID_PROVIDER + providerStr );
        }
        // 2. Perform OAuth Exchange (Mocked)
        log.info( LogMessages.CONNECT_INIT_TOKEN_EXCHANGE, provider );
        // --- START MOCK LOGIC ---
        // In a real scenario, this would be: TokenResponse tokens = providerClient.exchange(authCode);
        String mockProviderId = "mock_" + provider.name().toLowerCase() + "_" + UUID.randomUUID().toString();
        String mockAccessToken = "mock_access_token_" + UUID.randomUUID().toString();
        String mockRefreshToken = "mock_refresh_token_" + UUID.randomUUID().toString();
        // Assume tokens are valid for 60 days standard
        LocalDateTime expiry = LocalDateTime.now().plusDays( 60 );
        // --- END MOCK LOGIC ---
        log.debug( LogMessages.CONNECT_EXCHANGE_SUCCESS, mockProviderId, expiry );
        // 3. Build Entity
        SocialAccount account = SocialAccount.builder().provider( provider ).providerId( mockProviderId )
                .accessToken( mockAccessToken ).refreshToken( mockRefreshToken ).tokenExpiry( expiry )
                .ownerId( ownerId ).build();
        // 4. Persist
        try
        {
            SocialAccount savedAccount = socialAccountRepository.save( account );
            log.info( LogMessages.ACCOUNT_CONNECTED_INFO, provider, mockProviderId );
            log.info( LogMessages.CONNECT_EXIT_SUCCESS, savedAccount.getId() );
            return savedAccount;
        }
        catch ( Exception e )
        {
            log.error( LogMessages.CONNECT_DB_ERROR, provider, ownerId, e );
            throw new RuntimeException( ErrorMessages.DB_SAVE_FAILED, e );
        }
    }

    /**
     * Publishes a post to the specified social account.
     * 
     * <p><b>Functionality:</b></p>
     * <ol>
     *     <li>Retrieves the {@link SocialAccount} by ID.</li>
     *     <li>Validates the account exists.</li>
     *     <li>Constructs the API request payload for the provider.</li>
     *     <li>Executes the publish action (Mocked).</li>
     * </ol>
     *
     * <p><b>TODO:</b></p>
     * <ul>
     *     <li>Make this method asynchronous (@Async) to prevent blocking the caller during network I/O.</li>
     *     <li>Add retry logic with exponential backoff for network flakes.</li>
     *     <li>Handle token expiry (401 responses) by attempting a refresh flow automatically.</li>
     * </ul>
     *
     * @param accountId The UUID of the stored social account.
     * @param caption   The text content/caption of the post.
     * @param mediaUrl  The direct URL to the image or video asset.
     * @throws IllegalArgumentException if the account is not found.
     */
    public void publishPost( UUID accountId, String caption, String mediaUrl )
    {
        log.info( LogMessages.PUBLISH_ENTRY, accountId );
        // Do not log full caption or mediaUrl to avoid PII or log bloat
        log.debug( LogMessages.PUBLISH_DEBUG_INPUT, ( caption != null ? caption.length() : 0 ), ( mediaUrl != null ) );
        // 1. Retrieve Account
        SocialAccount account = socialAccountRepository.findById( accountId ).orElseThrow( () -> {
            log.error( LogMessages.PUBLISH_ACCOUNT_LOOKUP_FAILED, accountId );
            return new IllegalArgumentException( ErrorMessages.ACCOUNT_NOT_FOUND + accountId );
        } );
        // 2. Publish Logic (Mocked)
        try
        {
            log.info( LogMessages.PUBLISH_CALL_EXTERNAL_API, account.getProvider() );
            // --- START MOCK LOGIC ---
            // Simulating network latency
            // Thread.sleep(100); 
            log.info( LogMessages.LOG_SEPARATOR );
            log.info( LogMessages.PUBLISH_LOG_POST_DETAILS, account.getProvider(), caption );
            log.info( LogMessages.PUBLISH_LOG_TARGET_ID, account.getProviderId() );
            log.info( LogMessages.PUBLISH_LOG_MEDIA, mediaUrl );
            // Sanitize sensitive token in logs
            String sanitizedToken = ( account.getAccessToken() != null
                    && account.getAccessToken().length() > 5 ) ? account.getAccessToken().substring( 0, 5 ) + "***"
                                                               : "null";
            log.debug( LogMessages.PUBLISH_LOG_TOKEN_DEBUG, sanitizedToken );
            log.info( LogMessages.LOG_SEPARATOR );
            // --- END MOCK LOGIC ---
            log.info( LogMessages.PUBLISH_EXIT_SUCCESS, accountId );
        }
        catch ( Exception e )
        {
            log.error( LogMessages.PUBLISH_ERROR, account.getProvider(), e );
            throw new RuntimeException( ErrorMessages.PUBLISH_FAILED, e );
        }
    }
}
