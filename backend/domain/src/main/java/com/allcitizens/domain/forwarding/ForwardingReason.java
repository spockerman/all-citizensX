package com.allcitizens.domain.forwarding;

import java.util.Objects;
import java.util.UUID;

public class ForwardingReason {

    private UUID id;
    private String name;
    private String type;

    private ForwardingReason() {
    }

    public static ForwardingReason create(String name, String type) {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(type, "type must not be null");
        if (type.isEmpty()) {
            throw new IllegalArgumentException("type must not be empty");
        }

        var r = new ForwardingReason();
        r.id = UUID.randomUUID();
        r.name = name;
        r.type = type.length() > 1 ? type.substring(0, 1) : type;
        return r;
    }

    public static ForwardingReason reconstitute(UUID id, String name, String type) {
        var r = new ForwardingReason();
        r.id = id;
        r.name = name;
        r.type = type;
        return r;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
