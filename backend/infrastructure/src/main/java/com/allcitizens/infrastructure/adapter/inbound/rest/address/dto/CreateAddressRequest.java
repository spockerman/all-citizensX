package com.allcitizens.infrastructure.adapter.inbound.rest.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateAddressRequest(
    @NotNull UUID cityId,
    @NotBlank String stateCode,
    String zipCode,
    String street,
    String number,
    String complement,
    UUID districtId,
    String landmark,
    Double latitude,
    Double longitude
) {
}
