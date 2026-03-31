package com.allcitizens.infrastructure.adapter.inbound.rest.person.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UpdatePersonRequest(
    String fullName,
    String email,
    String taxId,
    String motherName,
    LocalDate birthDate,
    String gender,
    UUID addressId,
    Boolean active
) {
}
