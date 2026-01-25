package io.zaplink.redirect.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.zaplink.redirect.entity.DynamicQrCodeEntity;

/**
 * Repository for Dynamic QR code lookups.
 * Used for QR redirect resolution.
 */
@Repository
public interface DynamicQrCodeRepository
    extends
    JpaRepository<DynamicQrCodeEntity, Long>
{
    /**
     * Find QR code by QR key.
     * Primary method for QR redirect resolution.
     *
     * @param qrKey the QR key
     * @return Optional containing the QR entity if found
     */
    Optional<DynamicQrCodeEntity> findByQrKey( String qrKey );
}
