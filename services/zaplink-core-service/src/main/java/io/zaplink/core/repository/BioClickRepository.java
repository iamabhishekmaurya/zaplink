package io.zaplink.core.repository;

import io.zaplink.core.entity.BioClickEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BioClickRepository
    extends
    JpaRepository<BioClickEntity, Long>
{
}
