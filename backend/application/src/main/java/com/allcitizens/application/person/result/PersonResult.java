package com.allcitizens.application.person.result;

import com.allcitizens.domain.person.Person;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record PersonResult(
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

    public static PersonResult fromDomain(Person person) {
        return new PersonResult(
            person.getId(),
            person.getTenantId(),
            person.getType().name(),
            person.getFullName(),
            person.getEmail(),
            person.getTaxId(),
            person.getMotherName(),
            person.getBirthDate(),
            person.getGender() != null ? person.getGender().name() : null,
            person.getAddressId(),
            person.getKeycloakUserId(),
            person.isActive(),
            person.getCreatedAt(),
            person.getUpdatedAt()
        );
    }
}
