package com.allcitizens.infrastructure.adapter.inbound.rest.tenant.mapper;

import com.allcitizens.application.tenant.command.CreateTenantCommand;
import com.allcitizens.application.tenant.command.UpdateTenantCommand;
import com.allcitizens.application.tenant.result.TenantResult;
import com.allcitizens.infrastructure.adapter.inbound.rest.tenant.dto.CreateTenantRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.tenant.dto.TenantResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.tenant.dto.UpdateTenantRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TenantRestMapper {

    public CreateTenantCommand toCommand(CreateTenantRequest request) {
        return new CreateTenantCommand(
            request.name(),
            request.code()
        );
    }

    public UpdateTenantCommand toCommand(UpdateTenantRequest request) {
        return new UpdateTenantCommand(
            request.name(),
            request.active()
        );
    }

    public TenantResponse toResponse(TenantResult result) {
        return new TenantResponse(
            result.id(),
            result.name(),
            result.code(),
            result.active(),
            result.config(),
            result.createdAt(),
            result.updatedAt()
        );
    }

    public List<TenantResponse> toResponseList(List<TenantResult> results) {
        return results.stream()
            .map(this::toResponse)
            .toList();
    }
}
