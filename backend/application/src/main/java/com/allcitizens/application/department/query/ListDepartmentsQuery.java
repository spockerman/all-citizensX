package com.allcitizens.application.department.query;

import java.util.UUID;

public record ListDepartmentsQuery(
        UUID tenantId,
        int page,
        int size,
        String search
) {
    public ListDepartmentsQuery {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (size < 1) {
            throw new IllegalArgumentException("size must be >= 1");
        }
    }
}
