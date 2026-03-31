package com.allcitizens.infrastructure.adapter.inbound.rest.tenant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTenantRequest(
    @NotBlank String name,
    @NotBlank @Size(max = 20) String code
) {
}
