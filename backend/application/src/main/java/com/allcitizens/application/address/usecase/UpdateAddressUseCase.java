package com.allcitizens.application.address.usecase;

import com.allcitizens.application.address.command.UpdateAddressCommand;
import com.allcitizens.application.address.result.AddressResult;

import java.util.UUID;

public interface UpdateAddressUseCase {

    AddressResult execute(UUID id, UpdateAddressCommand command);
}
