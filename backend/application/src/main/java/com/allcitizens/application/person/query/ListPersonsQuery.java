package com.allcitizens.application.person.query;

import java.util.UUID;

public record ListPersonsQuery(
        UUID tenantId,
        int page,
        int size,
        String search
) {
    public ListPersonsQuery {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (size < 1) {
            throw new IllegalArgumentException("size must be >= 1");
        }
    }
}
