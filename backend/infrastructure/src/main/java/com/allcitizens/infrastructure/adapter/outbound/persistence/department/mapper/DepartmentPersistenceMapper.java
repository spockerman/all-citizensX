package com.allcitizens.infrastructure.adapter.outbound.persistence.department.mapper;

import com.allcitizens.domain.department.Department;
import com.allcitizens.infrastructure.adapter.outbound.persistence.department.entity.DepartmentJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class DepartmentPersistenceMapper {

    public Department toDomain(DepartmentJpaEntity entity) {
        return Department.reconstitute(
            entity.getId(),
            entity.getTenantId(),
            entity.getParentId(),
            entity.getName(),
            entity.getAbbreviation(),
            entity.getEmail(),
            entity.isActive(),
            entity.isCanRespond(),
            entity.isRoot(),
            entity.getIconUrl(),
            entity.getDisplayOrder(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public DepartmentJpaEntity toEntity(Department department) {
        var entity = new DepartmentJpaEntity();
        entity.setId(department.getId());
        entity.setTenantId(department.getTenantId());
        entity.setParentId(department.getParentId());
        entity.setName(department.getName());
        entity.setAbbreviation(department.getAbbreviation());
        entity.setEmail(department.getEmail());
        entity.setActive(department.isActive());
        entity.setCanRespond(department.isCanRespond());
        entity.setRoot(department.isRoot());
        entity.setIconUrl(department.getIconUrl());
        entity.setDisplayOrder(department.getDisplayOrder());
        entity.setCreatedAt(department.getCreatedAt());
        entity.setUpdatedAt(department.getUpdatedAt());
        return entity;
    }
}
