package io.zaplink.social.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.zaplink.social.common.enums.SocialProvider;
import io.zaplink.social.entity.SocialAccount;

/**
 * Data Access Object for {@link SocialAccount} entities.
 * <p>
 * Provides standard CRUD and custom finder methods to manage social account persistence.
 * </p>
 */
@Repository
public interface SocialAccountRepository
    extends
    JpaRepository<SocialAccount, UUID>
{
    /**
     * Retrieves all social accounts connected by a specific user.
     *
     * @param ownerId The UUID of the user.
     * @return A list of social accounts belonging to the user.
     */
    List<SocialAccount> findByOwnerId( UUID ownerId );

    /**
     * Finds a specific social account by its provider and the provider's external user ID.
     * <p>
     * Useful for checking if an account is already connected to avoid duplicates.
     * </p>
     *
     * @param provider   The social platform (e.g., INSTAGRAM).
     * @param providerId The unique user ID on that platform.
     * @return An Optional containing the account if found.
     */
    Optional<SocialAccount> findByProviderAndProviderId( SocialProvider provider, String providerId );
}
