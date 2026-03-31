package com.allcitizens.application.subject.result;

import com.allcitizens.domain.subject.Subject;

import java.time.Instant;
import java.util.UUID;

public record SubjectResult(
    UUID id,
    UUID tenantId,
    UUID departmentId,
    String name,
    boolean active,
    boolean visibleWeb,
    boolean visibleApp,
    Instant createdAt,
    Instant updatedAt
) {

    public static SubjectResult fromDomain(Subject subject) {
        return new SubjectResult(
            subject.getId(),
            subject.getTenantId(),
            subject.getDepartmentId(),
            subject.getName(),
            subject.isActive(),
            subject.isVisibleWeb(),
            subject.isVisibleApp(),
            subject.getCreatedAt(),
            subject.getUpdatedAt()
        );
    }
}
