package com.allcitizens.domain.subdivision;

import java.util.Objects;
import java.util.UUID;

public class Subdivision {

    private UUID id;
    private String name;
    private boolean active;

    private Subdivision() {
    }

    public static Subdivision create(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }

        var subdivision = new Subdivision();
        subdivision.id = UUID.randomUUID();
        subdivision.name = name;
        subdivision.active = true;
        return subdivision;
    }

    public static Subdivision reconstitute(UUID id, String name, boolean active) {
        var subdivision = new Subdivision();
        subdivision.id = id;
        subdivision.name = name;
        subdivision.active = active;
        return subdivision;
    }

    public void update(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.name = name;
    }

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
}
