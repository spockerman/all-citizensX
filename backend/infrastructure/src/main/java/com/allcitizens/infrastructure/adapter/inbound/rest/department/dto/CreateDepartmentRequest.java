package com.allcitizens.infrastructure.adapter.inbound.rest.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateDepartmentRequest(
    @NotNull UUID tenantId,
    UUID parentId,
    @NotBlank String name,
    String abbreviation,
    String email,
    boolean canRespond,
    boolean isRoot,
    String iconUrl,
    int displayOrder
) {
}
