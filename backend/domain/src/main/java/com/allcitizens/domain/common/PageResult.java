package com.allcitizens.domain.common;

import java.util.List;

public record PageResult<T>(
        List<T> content,
        long totalElements,
        int page,
        int size
) {
    public int totalPages() {
        if (size <= 0) {
            return 0;
        }
        return (int) Math.ceil((double) totalElements / size);
    }
}
