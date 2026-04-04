package com.allcitizens.infrastructure.adapter.outbound.persistence.tenant.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.tenant.entity.TenantJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface JpaTenantRepository extends JpaRepository<TenantJpaEntity, UUID> {

    Optional<TenantJpaEntity> findByCode(String code);

    boolean existsByCode(String code);

    Page<TenantJpaEntity> findAll(Pageable pageable);

    @Query("""
            SELECT t FROM TenantJpaEntity t
            WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(t.code) LIKE LOWER(CONCAT('%', :q, '%'))
            """)
    Page<TenantJpaEntity> search(@Param("q") String q, Pageable pageable);
}
