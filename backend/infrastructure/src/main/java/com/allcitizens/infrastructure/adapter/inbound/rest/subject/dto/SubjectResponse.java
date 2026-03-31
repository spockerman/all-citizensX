package com.allcitizens.infrastructure.adapter.inbound.rest.subject.dto;

import java.time.Instant;
import java.util.UUID;

public record SubjectResponse(
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
}
