package com.allcitizens.application.address.command;

import java.util.UUID;

public record CreateAddressCommand(
    UUID cityId,
    String stateCode,
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
