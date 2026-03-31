package com.allcitizens.application.address.result;

import com.allcitizens.domain.address.Address;

import java.time.Instant;
import java.util.UUID;

public record AddressResult(
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

    public static AddressResult fromDomain(Address address) {
        return new AddressResult(
            address.getId(),
            address.getZipCode(),
            address.getStreet(),
            address.getNumber(),
            address.getComplement(),
            address.getDistrictId(),
            address.getCityId(),
            address.getStateCode(),
            address.getLandmark(),
            address.getLatitude(),
            address.getLongitude(),
            address.getCreatedAt(),
            address.getUpdatedAt()
        );
    }
}
