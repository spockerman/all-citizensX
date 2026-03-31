package com.allcitizens.domain.department;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository {

    Department save(Department department);

    Optional<Department> findById(UUID id);

    List<Department> findAllByTenantId(UUID tenantId);

    List<Department> findByTenantIdAndParentIdIsNull(UUID tenantId);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
