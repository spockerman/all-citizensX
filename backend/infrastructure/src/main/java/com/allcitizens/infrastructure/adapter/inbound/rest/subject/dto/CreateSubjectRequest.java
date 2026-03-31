package com.allcitizens.infrastructure.adapter.inbound.rest.subject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateSubjectRequest(
    @NotNull UUID tenantId,
    UUID departmentId,
    @NotBlank String name,
    boolean visibleWeb,
    boolean visibleApp
) {
}
