package com.allcitizens.infrastructure.adapter.inbound.rest.person.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record CreatePersonRequest(
    @NotNull UUID tenantId,
    @NotBlank String fullName,
    String email,
    String taxId,
    String motherName,
    LocalDate birthDate,
    String gender,
    UUID addressId
) {
}
