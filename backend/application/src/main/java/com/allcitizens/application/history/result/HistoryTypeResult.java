package com.allcitizens.application.history.result;

import com.allcitizens.domain.history.HistoryType;

import java.util.UUID;

public record HistoryTypeResult(UUID id, String name) {

    public static HistoryTypeResult fromDomain(HistoryType h) {
        return new HistoryTypeResult(h.getId(), h.getName());
    }
}
