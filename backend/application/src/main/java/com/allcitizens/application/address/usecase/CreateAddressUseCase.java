package com.allcitizens.application.address.usecase;

import com.allcitizens.application.address.command.CreateAddressCommand;
import com.allcitizens.application.address.result.AddressResult;

public interface CreateAddressUseCase {

    AddressResult execute(CreateAddressCommand command);
}
