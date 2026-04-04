package com.allcitizens.application.history.command;

import java.util.Map;
import java.util.UUID;

public record AppendRequestHistoryCommand(
        UUID requestId,
        UUID historyTypeId,
        UUID userId,
        String description,
        Map<String, Object> previousData,
        Map<String, Object> newData
) {
}
