package com.allcitizens.domain.catalog;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CatalogServiceRepository {
    CatalogService save(CatalogService catalogService);
    Optional<CatalogService> findById(UUID id);
    List<CatalogService> findAllByTenantId(UUID tenantId);
    List<CatalogService> findAllBySubjectId(UUID subjectId);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
