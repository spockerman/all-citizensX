package com.allcitizens.infrastructure.adapter.outbound.persistence.department.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.department.entity.DepartmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaDepartmentRepository extends JpaRepository<DepartmentJpaEntity, UUID> {

    List<DepartmentJpaEntity> findAllByTenantId(UUID tenantId);

    List<DepartmentJpaEntity> findByTenantIdAndParentIdIsNull(UUID tenantId);
}
