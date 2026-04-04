package com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto;

import java.time.Instant;
import java.util.UUID;

public record RedistributionResponse(
        UUID id,
        UUID forwardingId,
        UUID targetDepartmentId,
        UUID userId,
        String status,
        boolean read,
        String notes,
        Instant createdAt
) {
}
