package com.allcitizens.infrastructure.adapter.inbound.rest.department.mapper;

import com.allcitizens.application.department.command.CreateDepartmentCommand;
import com.allcitizens.application.department.command.UpdateDepartmentCommand;
import com.allcitizens.application.department.result.DepartmentResult;
import com.allcitizens.infrastructure.adapter.inbound.rest.department.dto.CreateDepartmentRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.department.dto.DepartmentResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.department.dto.UpdateDepartmentRequest;
import org.springframework.stereotype.Component;

@Component
public class DepartmentRestMapper {

    public CreateDepartmentCommand toCommand(CreateDepartmentRequest request) {
        return new CreateDepartmentCommand(
            request.tenantId(),
            request.parentId(),
            request.name(),
            request.abbreviation(),
            request.email(),
            request.canRespond(),
            request.isRoot(),
            request.iconUrl(),
            request.displayOrder()
        );
    }

    public UpdateDepartmentCommand toCommand(UpdateDepartmentRequest request) {
        return new UpdateDepartmentCommand(
            request.name(),
            request.abbreviation(),
            request.email(),
            request.canRespond(),
            request.active(),
            request.parentId(),
            request.displayOrder()
        );
    }

    public DepartmentResponse toResponse(DepartmentResult result) {
        return new DepartmentResponse(
            result.id(),
            result.tenantId(),
            result.parentId(),
            result.name(),
            result.abbreviation(),
            result.email(),
            result.active(),
            result.canRespond(),
            result.isRoot(),
            result.iconUrl(),
            result.displayOrder(),
            result.createdAt(),
            result.updatedAt()
        );
    }
}
