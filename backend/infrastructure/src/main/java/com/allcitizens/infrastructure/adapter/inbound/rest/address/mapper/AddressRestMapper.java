package com.allcitizens.infrastructure.adapter.inbound.rest.address.mapper;

import com.allcitizens.application.address.command.CreateAddressCommand;
import com.allcitizens.application.address.command.UpdateAddressCommand;
import com.allcitizens.application.address.result.AddressResult;
import com.allcitizens.infrastructure.adapter.inbound.rest.address.dto.AddressResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.address.dto.CreateAddressRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.address.dto.UpdateAddressRequest;
import org.springframework.stereotype.Component;

@Component
public class AddressRestMapper {

    public CreateAddressCommand toCommand(CreateAddressRequest request) {
        return new CreateAddressCommand(
            request.cityId(),
            request.stateCode(),
            request.zipCode(),
            request.street(),
            request.number(),
            request.complement(),
            request.districtId(),
            request.landmark(),
            request.latitude(),
            request.longitude()
        );
    }

    public UpdateAddressCommand toCommand(UpdateAddressRequest request) {
        return new UpdateAddressCommand(
            request.zipCode(),
            request.street(),
            request.number(),
            request.complement(),
            request.districtId(),
            request.landmark(),
            request.latitude(),
            request.longitude()
        );
    }

    public AddressResponse toResponse(AddressResult result) {
        return new AddressResponse(
            result.id(),
            result.zipCode(),
            result.street(),
            result.number(),
            result.complement(),
            result.districtId(),
            result.cityId(),
            result.stateCode(),
            result.landmark(),
            result.latitude(),
            result.longitude(),
            result.createdAt(),
            result.updatedAt()
        );
    }
}
