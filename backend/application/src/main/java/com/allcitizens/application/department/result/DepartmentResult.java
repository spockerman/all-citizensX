package com.allcitizens.application.department.result;

import com.allcitizens.domain.department.Department;

import java.time.Instant;
import java.util.UUID;

public record DepartmentResult(
    UUID id,
    UUID tenantId,
    UUID parentId,
    String name,
    String abbreviation,
    String email,
    boolean active,
    boolean canRespond,
    boolean isRoot,
    String iconUrl,
    int displayOrder,
    Instant createdAt,
    Instant updatedAt
) {

    public static DepartmentResult fromDomain(Department department) {
        return new DepartmentResult(
            department.getId(),
            department.getTenantId(),
            department.getParentId(),
            department.getName(),
            department.getAbbreviation(),
            department.getEmail(),
            department.isActive(),
            department.isCanRespond(),
            department.isRoot(),
            department.getIconUrl(),
            department.getDisplayOrder(),
            department.getCreatedAt(),
            department.getUpdatedAt()
        );
    }
}
