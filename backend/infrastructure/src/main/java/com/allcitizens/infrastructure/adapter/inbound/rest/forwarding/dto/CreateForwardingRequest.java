package com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record CreateForwardingRequest(
        @NotNull UUID targetDepartmentId,
        UUID sourceDepartmentId,
        UUID reasonId,
        UUID userId,
        String notes,
        LocalDate dueDate
) {
}
