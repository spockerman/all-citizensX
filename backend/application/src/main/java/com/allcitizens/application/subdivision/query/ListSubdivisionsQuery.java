package com.allcitizens.application.subdivision.query;

public record ListSubdivisionsQuery(
        int page,
        int size,
        String search
) {
    public ListSubdivisionsQuery {
        if (page < 0) {
            throw new IllegalArgumentException("page must be >= 0");
        }
        if (size < 1) {
            throw new IllegalArgumentException("size must be >= 1");
        }
    }
}
