package com.allcitizens.application.address.usecase;

import com.allcitizens.application.address.result.AddressResult;

import java.util.UUID;

public interface GetAddressUseCase {

    AddressResult execute(UUID id);
}
