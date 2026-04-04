package com.allcitizens.application.subject.query;

import java.util.UUID;

public record ListSubjectsQuery(
        UUID tenantId,
        int page,
        int size,
        String search
) {
    public ListSubjectsQuery {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (size < 1) {
            throw new IllegalArgumentException("size must be >= 1");
        }
    }
}
