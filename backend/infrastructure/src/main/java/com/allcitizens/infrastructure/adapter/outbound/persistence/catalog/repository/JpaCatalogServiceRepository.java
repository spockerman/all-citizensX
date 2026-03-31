package com.allcitizens.infrastructure.adapter.outbound.persistence.catalog.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.catalog.entity.CatalogServiceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaCatalogServiceRepository extends JpaRepository<CatalogServiceJpaEntity, UUID> {

    List<CatalogServiceJpaEntity> findAllByTenantId(UUID tenantId);

    List<CatalogServiceJpaEntity> findAllBySubjectId(UUID subjectId);
}
