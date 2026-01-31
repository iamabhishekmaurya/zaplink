package io.zaplink.manager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.zaplink.manager.entity.BioPageEntity;

@Repository
public interface BioPageRepository extends JpaRepository<BioPageEntity, Long> {
    
    Optional<BioPageEntity> findByUsername(String username);
    
    boolean existsByUsername(String username);
    
    @Query("SELECT bp FROM BioPageEntity bp LEFT JOIN FETCH bp.bioLinks WHERE bp.username = :username")
    Optional<BioPageEntity> findByUsernameWithLinks(@Param("username") String username);
    
    List<BioPageEntity> findByOwnerId(String ownerId);
    
    Page<BioPageEntity> findByOwnerId(String ownerId, Pageable pageable);
}
