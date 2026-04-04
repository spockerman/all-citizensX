package com.allcitizens.application.history.result;

import com.allcitizens.domain.history.RequestHistory;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record RequestHistoryResult(
        UUID id,
        UUID requestId,
        UUID historyTypeId,
        UUID userId,
        String description,
        Map<String, Object> previousData,
        Map<String, Object> newData,
        Instant createdAt
) {

    public static RequestHistoryResult fromDomain(RequestHistory h) {
        return new RequestHistoryResult(
                h.getId(), h.getRequestId(), h.getHistoryTypeId(), h.getUserId(),
                h.getDescription(), h.getPreviousData(), h.getNewData(), h.getCreatedAt()
        );
    }
}
