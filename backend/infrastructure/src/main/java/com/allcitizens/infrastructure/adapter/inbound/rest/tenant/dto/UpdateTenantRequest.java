package com.allcitizens.infrastructure.adapter.inbound.rest.tenant.dto;

public record UpdateTenantRequest(
    String name,
    Boolean active
) {
}
