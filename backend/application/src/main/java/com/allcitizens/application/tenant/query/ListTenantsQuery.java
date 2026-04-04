package com.allcitizens.application.tenant.query;

public record ListTenantsQuery(
        int page,
        int size,
        String search
) {
    public ListTenantsQuery {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (size < 1) {
            throw new IllegalArgumentException("size must be >= 1");
        }
    }
}
