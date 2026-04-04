package com.allcitizens.domain.subject;

import com.allcitizens.domain.common.PageResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubjectRepository {
    Subject save(Subject subject);
    Optional<Subject> findById(UUID id);
    List<Subject> findAllByTenantId(UUID tenantId);
    PageResult<Subject> findAllByTenantIdPaged(UUID tenantId, int page, int size);
    PageResult<Subject> searchByTenantIdPaged(UUID tenantId, String query, int page, int size);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
