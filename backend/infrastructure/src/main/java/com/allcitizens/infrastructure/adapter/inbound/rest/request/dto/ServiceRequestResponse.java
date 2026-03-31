package com.allcitizens.infrastructure.adapter.inbound.rest.request.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public record ServiceRequestResponse(
        UUID id,
        UUID tenantId,
        String protocol,
        UUID serviceId,
        UUID personId,
        UUID addressId,
        UUID requestTypeId,
        UUID incidentId,
        UUID responseMethodId,
        String channel,
        String status,
        String priority,
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
        String emotionalState,
        Instant createdAt,
        Instant updatedAt,
        Instant closedAt
) {
}
