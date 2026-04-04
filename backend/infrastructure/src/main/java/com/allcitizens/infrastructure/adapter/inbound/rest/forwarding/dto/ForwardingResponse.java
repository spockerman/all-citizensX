package com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ForwardingResponse(
        UUID id,
        UUID requestId,
        UUID targetDepartmentId,
        UUID sourceDepartmentId,
        UUID reasonId,
        UUID userId,
        String status,
        String notes,
        LocalDate dueDate,
        boolean read,
        Instant readAt,
        Instant createdAt,
        Instant updatedAt,
        Instant answeredAt
) {
}
