package com.allcitizens.infrastructure.adapter.inbound.rest.catalog.mapper;

import com.allcitizens.application.catalog.command.CreateCatalogServiceCommand;
import com.allcitizens.application.catalog.command.UpdateCatalogServiceCommand;
import com.allcitizens.application.catalog.result.CatalogServiceResult;
import com.allcitizens.infrastructure.adapter.inbound.rest.catalog.dto.CatalogServiceResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.catalog.dto.CreateCatalogServiceRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.catalog.dto.UpdateCatalogServiceRequest;
import org.springframework.stereotype.Component;

@Component
public class CatalogServiceRestMapper {

    public CreateCatalogServiceCommand toCommand(CreateCatalogServiceRequest request) {
        return new CreateCatalogServiceCommand(
            request.tenantId(),
            request.subjectId(),
            request.subdivisionId(),
            request.departmentId(),
            request.displayName(),
            request.description(),
            request.slaDays(),
            request.defaultPriority(),
            request.allowsAnonymous(),
            request.allowsMultiForward(),
            request.visibleWeb(),
            request.visibleApp()
        );
    }

    public UpdateCatalogServiceCommand toCommand(UpdateCatalogServiceRequest request) {
        return new UpdateCatalogServiceCommand(
            request.displayName(),
            request.description(),
            request.slaDays(),
            request.defaultPriority(),
            request.allowsAnonymous(),
            request.allowsMultiForward(),
            request.visibleWeb(),
            request.visibleApp(),
            request.active(),
            request.departmentId()
        );
    }

    public CatalogServiceResponse toResponse(CatalogServiceResult result) {
        return new CatalogServiceResponse(
            result.id(),
            result.tenantId(),
            result.subjectId(),
            result.subdivisionId(),
            result.departmentId(),
            result.displayName(),
            result.description(),
            result.slaDays(),
            result.defaultPriority(),
            result.allowsAnonymous(),
            result.allowsMultiForward(),
            result.visibleWeb(),
            result.visibleApp(),
            result.dynamicFields(),
            result.active(),
            result.createdAt(),
            result.updatedAt()
        );
    }
}
