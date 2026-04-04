package com.allcitizens.domain.history;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class RequestHistory {

    private UUID id;
    private UUID requestId;
    private UUID historyTypeId;
    private UUID userId;
    private String description;
    private Map<String, Object> previousData;
    private Map<String, Object> newData;
    private Instant createdAt;

    private RequestHistory() {
    }

    public static RequestHistory create(UUID requestId, UUID historyTypeId, UUID userId,
                                        String description,
                                        Map<String, Object> previousData,
                                        Map<String, Object> newData) {
        var h = new RequestHistory();
        h.id = UUID.randomUUID();
        h.requestId = requestId;
        h.historyTypeId = historyTypeId;
        h.userId = userId;
        h.description = description;
        h.previousData = previousData != null ? previousData : Map.of();
        h.newData = newData != null ? newData : Map.of();
        h.createdAt = Instant.now();
        return h;
    }

    public static RequestHistory reconstitute(UUID id, UUID requestId, UUID historyTypeId,
                                              UUID userId, String description,
                                              Map<String, Object> previousData,
                                              Map<String, Object> newData,
                                              Instant createdAt) {
        var h = new RequestHistory();
        h.id = id;
        h.requestId = requestId;
        h.historyTypeId = historyTypeId;
        h.userId = userId;
        h.description = description;
        h.previousData = previousData != null ? previousData : Map.of();
        h.newData = newData != null ? newData : Map.of();
        h.createdAt = createdAt;
        return h;
    }

    public UUID getId() {
        return id;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public UUID getHistoryTypeId() {
        return historyTypeId;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Object> getPreviousData() {
        return previousData;
    }

    public Map<String, Object> getNewData() {
        return newData;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
