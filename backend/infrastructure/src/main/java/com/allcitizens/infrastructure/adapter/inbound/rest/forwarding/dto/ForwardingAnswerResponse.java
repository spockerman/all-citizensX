package com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto;

import java.time.Instant;
import java.util.UUID;

public record ForwardingAnswerResponse(
        UUID id,
        UUID forwardingId,
        UUID departmentId,
        UUID userId,
        UUID reasonId,
        String response,
        Instant createdAt
) {
}
