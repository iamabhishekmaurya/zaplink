package io.zaplink.social.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.zaplink.social.common.enums.SocialProvider;
import io.zaplink.social.entity.SocialAccount;

@Repository
public interface SocialAccountRepository
    extends
    JpaRepository<SocialAccount, UUID>
{
    List<SocialAccount> findByOwnerId( UUID ownerId );

    Optional<SocialAccount> findByProviderAndProviderId( SocialProvider provider, String providerId );
}
