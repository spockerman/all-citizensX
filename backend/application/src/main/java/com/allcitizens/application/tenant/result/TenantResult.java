package com.allcitizens.application.tenant.result;

import com.allcitizens.domain.tenant.Tenant;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record TenantResult(
    UUID id,
    String name,
    String code,
    boolean active,
    Map<String, Object> config,
    Instant createdAt,
    Instant updatedAt
) {

    public static TenantResult fromDomain(Tenant tenant) {
        return new TenantResult(
            tenant.getId(),
            tenant.getName(),
            tenant.getCode(),
            tenant.isActive(),
            tenant.getConfig(),
            tenant.getCreatedAt(),
            tenant.getUpdatedAt()
        );
    }
}
