package com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.mapper;

import com.allcitizens.application.subdivision.command.CreateSubdivisionCommand;
import com.allcitizens.application.subdivision.command.UpdateSubdivisionCommand;
import com.allcitizens.application.subdivision.result.SubdivisionResult;
import com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.dto.CreateSubdivisionRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.dto.SubdivisionResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.dto.UpdateSubdivisionRequest;
import org.springframework.stereotype.Component;

@Component
public class SubdivisionRestMapper {

    public CreateSubdivisionCommand toCommand(CreateSubdivisionRequest request) {
        return new CreateSubdivisionCommand(request.name());
    }

    public UpdateSubdivisionCommand toCommand(UpdateSubdivisionRequest request) {
        return new UpdateSubdivisionCommand(request.name(), request.active());
    }

    public SubdivisionResponse toResponse(SubdivisionResult result) {
        return new SubdivisionResponse(result.id(), result.name(), result.active());
    }
}
