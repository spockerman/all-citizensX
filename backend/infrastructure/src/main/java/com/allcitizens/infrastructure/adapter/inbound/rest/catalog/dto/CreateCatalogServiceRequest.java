package com.allcitizens.infrastructure.adapter.inbound.rest.catalog.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCatalogServiceRequest(
    @NotNull UUID tenantId,
    @NotNull UUID subjectId,
    @NotNull UUID subdivisionId,
    UUID departmentId,
    String displayName,
    String description,
    int slaDays,
    String defaultPriority,
    boolean allowsAnonymous,
    boolean allowsMultiForward,
    boolean visibleWeb,
    boolean visibleApp
) {
}
