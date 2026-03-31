package com.allcitizens.infrastructure.adapter.inbound.rest.department.dto;

import java.time.Instant;
import java.util.UUID;

public record DepartmentResponse(
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
}
