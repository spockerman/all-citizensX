package com.allcitizens.infrastructure.adapter.inbound.rest.history.dto;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record RequestHistoryResponse(
        UUID id,
        UUID requestId,
        UUID historyTypeId,
        UUID userId,
        String description,
        Map<String, Object> previousData,
        Map<String, Object> newData,
        Instant createdAt
) {
}
