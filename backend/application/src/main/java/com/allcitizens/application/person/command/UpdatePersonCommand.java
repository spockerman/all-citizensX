package com.allcitizens.application.person.command;

import java.time.LocalDate;
import java.util.UUID;

public record UpdatePersonCommand(
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
