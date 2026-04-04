package com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateRedistributionRequest(
        @NotNull UUID targetDepartmentId,
        UUID userId,
        String notes
) {
}
