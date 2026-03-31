package com.allcitizens.infrastructure.adapter.inbound.rest.person.mapper;

import com.allcitizens.application.person.command.CreatePersonCommand;
import com.allcitizens.application.person.command.UpdatePersonCommand;
import com.allcitizens.application.person.result.PersonResult;
import com.allcitizens.infrastructure.adapter.inbound.rest.person.dto.CreatePersonRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.person.dto.PersonResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.person.dto.UpdatePersonRequest;
import org.springframework.stereotype.Component;

@Component
public class PersonRestMapper {

    public CreatePersonCommand toCommand(CreatePersonRequest request) {
        return new CreatePersonCommand(
            request.tenantId(),
            request.fullName(),
            request.email(),
            request.taxId(),
            request.motherName(),
            request.birthDate(),
            request.gender(),
            request.addressId()
        );
    }

    public UpdatePersonCommand toCommand(UpdatePersonRequest request) {
        return new UpdatePersonCommand(
            request.fullName(),
            request.email(),
            request.taxId(),
            request.motherName(),
            request.birthDate(),
            request.gender(),
            request.addressId(),
            request.active()
        );
    }

    public PersonResponse toResponse(PersonResult result) {
        return new PersonResponse(
            result.id(),
            result.tenantId(),
            result.type(),
            result.fullName(),
            result.email(),
            result.taxId(),
            result.motherName(),
            result.birthDate(),
            result.gender(),
            result.addressId(),
            result.keycloakUserId(),
            result.active(),
            result.createdAt(),
            result.updatedAt()
        );
    }
}
