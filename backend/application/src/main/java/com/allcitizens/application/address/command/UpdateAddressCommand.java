package com.allcitizens.application.address.command;

import java.util.UUID;

public record UpdateAddressCommand(
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
