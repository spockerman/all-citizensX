package com.allcitizens.domain.catalog;

import com.allcitizens.domain.common.PageResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CatalogServiceRepository {
    CatalogService save(CatalogService catalogService);
    Optional<CatalogService> findById(UUID id);
    List<CatalogService> findAllByTenantId(UUID tenantId);
    PageResult<CatalogService> findAllByTenantIdPaged(UUID tenantId, int page, int size);
    PageResult<CatalogService> searchByTenantIdPaged(UUID tenantId, String query, int page, int size);
    List<CatalogService> findAllBySubjectId(UUID subjectId);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
