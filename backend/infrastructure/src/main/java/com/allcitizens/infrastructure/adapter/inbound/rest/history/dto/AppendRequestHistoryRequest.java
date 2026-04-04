package com.allcitizens.infrastructure.adapter.inbound.rest.history.dto;

import java.util.Map;
import java.util.UUID;

public record AppendRequestHistoryRequest(
        UUID historyTypeId,
        UUID userId,
        String description,
        Map<String, Object> previousData,
        Map<String, Object> newData
) {
}
