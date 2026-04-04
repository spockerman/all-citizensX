package com.allcitizens.application.request.query;

import java.util.UUID;

public record ListServiceRequestsQuery(
        UUID tenantId,
        int page,
        int size,
        String search
) {
    public ListServiceRequestsQuery {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (size < 1) {
            throw new IllegalArgumentException("size must be >= 1");
        }
    }
}
