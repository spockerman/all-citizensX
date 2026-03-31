package com.allcitizens.infrastructure.adapter.inbound.rest.person.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record PersonResponse(
    UUID id,
    UUID tenantId,
    String type,
    String fullName,
    String email,
    String taxId,
    String motherName,
    LocalDate birthDate,
    String gender,
    UUID addressId,
    UUID keycloakUserId,
    boolean active,
    Instant createdAt,
    Instant updatedAt
) {
}
