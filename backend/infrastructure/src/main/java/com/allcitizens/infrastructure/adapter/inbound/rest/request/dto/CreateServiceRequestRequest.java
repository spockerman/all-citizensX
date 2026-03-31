package com.allcitizens.infrastructure.adapter.inbound.rest.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record CreateServiceRequestRequest(
        @NotNull UUID tenantId,
        @NotBlank String protocol,
        @NotNull UUID serviceId,
        UUID personId,
        UUID addressId,
        String channel,
        String priority,
        String description,
        boolean confidential,
        boolean anonymous,
        Map<String, Object> dynamicFields,
        Double latitude,
        Double longitude
) {
}
