package com.allcitizens.infrastructure.adapter.inbound.rest.address.dto;

import java.util.UUID;

public record UpdateAddressRequest(
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
