package com.allcitizens.infrastructure.adapter.inbound.rest.tenant.dto;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record TenantResponse(
    UUID id,
    String name,
    String code,
    boolean active,
    Map<String, Object> config,
    Instant createdAt,
    Instant updatedAt
) {
}
