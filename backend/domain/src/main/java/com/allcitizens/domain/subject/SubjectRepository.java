package com.allcitizens.domain.subject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubjectRepository {
    Subject save(Subject subject);
    Optional<Subject> findById(UUID id);
    List<Subject> findAllByTenantId(UUID tenantId);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
