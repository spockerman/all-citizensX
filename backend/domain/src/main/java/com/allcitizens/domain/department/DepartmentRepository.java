package com.allcitizens.domain.department;

import com.allcitizens.domain.common.PageResult;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository {

    Department save(Department department);

    Optional<Department> findById(UUID id);

    List<Department> findAllByTenantId(UUID tenantId);

    PageResult<Department> findAllByTenantIdPaged(UUID tenantId, int page, int size);

    PageResult<Department> searchByTenantIdPaged(UUID tenantId, String query, int page, int size);

    List<Department> findByTenantIdAndParentIdIsNull(UUID tenantId);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
