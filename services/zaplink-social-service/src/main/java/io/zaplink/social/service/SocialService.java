package io.zaplink.social.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.zaplink.social.common.constants.Constants;
import io.zaplink.social.common.enums.SocialProvider;
import io.zaplink.social.entity.SocialAccount;
import io.zaplink.social.repository.SocialAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @RequiredArgsConstructor @Slf4j
public class SocialService
{
    private final SocialAccountRepository socialAccountRepository;
    public SocialAccount connectAccount( String providerStr, String authCode, UUID ownerId )
    {
        SocialProvider provider = SocialProvider.valueOf( providerStr.toUpperCase() );
        // MOCK OAUTH EXCHANGE
        String mockProviderId = "mock_" + provider.name().toLowerCase() + "_" + UUID.randomUUID().toString();
        String mockAccessToken = "mock_access_token_" + UUID.randomUUID().toString();
        String mockRefreshToken = "mock_refresh_token_" + UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusDays( 60 );
        SocialAccount account = SocialAccount.builder().provider( provider ).providerId( mockProviderId )
                .accessToken( mockAccessToken ).refreshToken( mockRefreshToken ).tokenExpiry( expiry )
                .ownerId( ownerId ).build();
        log.info( Constants.LOG_ACCOUNT_CONNECTED, provider, mockProviderId );
        return socialAccountRepository.save( account );
    }

    public void publishPost( UUID accountId, String caption, String mediaUrl )
    {
        SocialAccount account = socialAccountRepository.findById( accountId )
                .orElseThrow( () -> new IllegalArgumentException( Constants.EXCEPTION_ACCOUNT_NOT_FOUND + accountId ) );
        // MOCK PUBLISH
        log.info( "--------------------------------------------------" );
        log.info( Constants.LOG_PUBLISHING_POST, account.getProvider(), caption );
        log.info( "Media URL: {}", mediaUrl );
        log.info( "Account ID: {}", account.getId() );
        log.info( "--------------------------------------------------" );
    }
}
