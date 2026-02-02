package io.zaplink.core.repository;

import io.zaplink.core.entity.BioPageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface BioPageRepository
    extends
    JpaRepository<BioPageEntity, Long>
{
    Optional<BioPageEntity> findByUsername( String username );

    List<BioPageEntity> findByOwnerId( String ownerId );

    boolean existsByUsername( String username );
}
