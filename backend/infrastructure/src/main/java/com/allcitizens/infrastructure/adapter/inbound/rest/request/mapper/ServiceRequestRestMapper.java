package com.allcitizens.infrastructure.adapter.inbound.rest.request.mapper;

import com.allcitizens.application.request.command.CreateServiceRequestCommand;
import com.allcitizens.application.request.command.UpdateServiceRequestCommand;
import com.allcitizens.application.request.result.ServiceRequestResult;
import com.allcitizens.domain.request.Channel;
import com.allcitizens.domain.request.Priority;
import com.allcitizens.infrastructure.adapter.inbound.rest.request.dto.CreateServiceRequestRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.request.dto.ServiceRequestResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.request.dto.UpdateServiceRequestRequest;
import org.springframework.stereotype.Component;

@Component
public class ServiceRequestRestMapper {

    public CreateServiceRequestCommand toCommand(CreateServiceRequestRequest request) {
        return new CreateServiceRequestCommand(
                request.tenantId(),
                request.protocol(),
                request.serviceId(),
                request.personId(),
                request.addressId(),
                request.channel() != null ? Channel.valueOf(request.channel()) : null,
                request.priority() != null ? Priority.valueOf(request.priority()) : null,
                request.description(),
                request.confidential(),
                request.anonymous(),
                request.dynamicFields(),
                request.latitude(),
                request.longitude()
        );
    }

    public UpdateServiceRequestCommand toCommand(UpdateServiceRequestRequest request) {
        return new UpdateServiceRequestCommand(
                request.description(),
                request.internalNote(),
                request.personId(),
                request.addressId(),
                request.priority() != null ? Priority.valueOf(request.priority()) : null
        );
    }

    public ServiceRequestResponse toResponse(ServiceRequestResult result) {
        return new ServiceRequestResponse(
                result.id(),
                result.tenantId(),
                result.protocol(),
                result.serviceId(),
                result.personId(),
                result.addressId(),
                result.requestTypeId(),
                result.incidentId(),
                result.responseMethodId(),
                result.channel() != null ? result.channel().name() : null,
                result.status() != null ? result.status().name() : null,
                result.priority() != null ? result.priority().name() : null,
                result.description(),
                result.internalNote(),
                result.dynamicFields(),
                result.latitude(),
                result.longitude(),
                result.confidential(),
                result.anonymous(),
                result.externalDocType(),
                result.externalDocNumber(),
                result.dueDate(),
                result.agentUserId(),
                result.emotionalState() != null ? result.emotionalState().name() : null,
                result.createdAt(),
                result.updatedAt(),
                result.closedAt()
        );
    }
}
