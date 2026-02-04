package io.zaplink.core.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.zaplink.core.entity.BioPageEntity;

@Repository
public interface BioPageRepository
    extends
    JpaRepository<BioPageEntity, Long>
{
    Optional<BioPageEntity> findByUsername( String username );

    List<BioPageEntity> findByOwnerId( String ownerId );

    boolean existsByUsername( String username );
}
