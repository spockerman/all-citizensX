package com.allcitizens.infrastructure.adapter.inbound.rest.catalog.dto;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record CatalogServiceResponse(
    UUID id,
    UUID tenantId,
    UUID subjectId,
    UUID subdivisionId,
    UUID departmentId,
    String displayName,
    String description,
    int slaDays,
    String defaultPriority,
    boolean allowsAnonymous,
    boolean allowsMultiForward,
    boolean visibleWeb,
    boolean visibleApp,
    Map<String, Object> dynamicFields,
    boolean active,
    Instant createdAt,
    Instant updatedAt
) {
}
