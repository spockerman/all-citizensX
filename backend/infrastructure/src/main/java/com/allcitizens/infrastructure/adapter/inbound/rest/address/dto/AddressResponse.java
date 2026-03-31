package com.allcitizens.infrastructure.adapter.inbound.rest.address.dto;

import java.time.Instant;
import java.util.UUID;

public record AddressResponse(
    UUID id,
    String zipCode,
    String street,
    String number,
    String complement,
    UUID districtId,
    UUID cityId,
    String stateCode,
    String landmark,
    Double latitude,
    Double longitude,
    Instant createdAt,
    Instant updatedAt
) {
}
