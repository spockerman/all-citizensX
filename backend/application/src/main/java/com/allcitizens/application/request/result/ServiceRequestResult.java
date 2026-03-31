package com.allcitizens.application.request.result;

import com.allcitizens.domain.request.Channel;
import com.allcitizens.domain.request.EmotionalState;
import com.allcitizens.domain.request.Priority;
import com.allcitizens.domain.request.RequestStatus;
import com.allcitizens.domain.request.ServiceRequest;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public record ServiceRequestResult(
        UUID id,
        UUID tenantId,
        String protocol,
        UUID serviceId,
        UUID personId,
        UUID addressId,
        UUID requestTypeId,
        UUID incidentId,
        UUID responseMethodId,
        Channel channel,
        RequestStatus status,
        Priority priority,
        String description,
        String internalNote,
        Map<String, Object> dynamicFields,
        Double latitude,
        Double longitude,
        boolean confidential,
        boolean anonymous,
        String externalDocType,
        String externalDocNumber,
        LocalDate dueDate,
        UUID agentUserId,
        EmotionalState emotionalState,
        Instant createdAt,
        Instant updatedAt,
        Instant closedAt
) {

    public static ServiceRequestResult fromDomain(ServiceRequest sr) {
        return new ServiceRequestResult(
                sr.getId(), sr.getTenantId(), sr.getProtocol(), sr.getServiceId(),
                sr.getPersonId(), sr.getAddressId(), sr.getRequestTypeId(),
                sr.getIncidentId(), sr.getResponseMethodId(), sr.getChannel(),
                sr.getStatus(), sr.getPriority(), sr.getDescription(),
                sr.getInternalNote(), sr.getDynamicFields(), sr.getLatitude(),
                sr.getLongitude(), sr.isConfidential(), sr.isAnonymous(),
                sr.getExternalDocType(), sr.getExternalDocNumber(), sr.getDueDate(),
                sr.getAgentUserId(), sr.getEmotionalState(), sr.getCreatedAt(),
                sr.getUpdatedAt(), sr.getClosedAt()
        );
    }
}
