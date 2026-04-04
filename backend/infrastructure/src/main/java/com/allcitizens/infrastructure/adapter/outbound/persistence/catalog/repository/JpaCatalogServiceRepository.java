package com.allcitizens.infrastructure.adapter.outbound.persistence.catalog.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.catalog.entity.CatalogServiceJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaCatalogServiceRepository extends JpaRepository<CatalogServiceJpaEntity, UUID> {

    List<CatalogServiceJpaEntity> findAllByTenantId(UUID tenantId);

    Page<CatalogServiceJpaEntity> findAllByTenantId(UUID tenantId, Pageable pageable);

    @Query("""
            SELECT c FROM CatalogServiceJpaEntity c
            WHERE c.tenantId = :tenantId
              AND (
                LOWER(c.displayName) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(COALESCE(c.description, '')) LIKE LOWER(CONCAT('%', :q, '%'))
              )
            """)
    Page<CatalogServiceJpaEntity> searchByTenantId(
            @Param("tenantId") UUID tenantId,
            @Param("q") String q,
            Pageable pageable);

    List<CatalogServiceJpaEntity> findAllBySubjectId(UUID subjectId);
}
