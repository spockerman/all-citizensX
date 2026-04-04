package com.allcitizens.domain.history;

import java.util.Objects;
import java.util.UUID;

public class HistoryType {

    private UUID id;
    private String name;

    private HistoryType() {
    }

    public static HistoryType create(String name) {
        Objects.requireNonNull(name, "name must not be null");

        var h = new HistoryType();
        h.id = UUID.randomUUID();
        h.name = name;
        return h;
    }

    public static HistoryType reconstitute(UUID id, String name) {
        var h = new HistoryType();
        h.id = id;
        h.name = name;
        return h;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
