package com.allcitizens.domain.tenant;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Tenant {

    private UUID id;
    private String name;
    private String code;
    private boolean active;
    private Map<String, Object> config;
    private Instant createdAt;
    private Instant updatedAt;

    private Tenant() {
    }

    public static Tenant create(String name, String code) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("code must not be blank");
        }
        if (code.length() > 20) {
            throw new IllegalArgumentException("code must not exceed 20 characters");
        }

        var tenant = new Tenant();
        tenant.id = UUID.randomUUID();
        tenant.name = name;
        tenant.code = code;
        tenant.active = true;
        tenant.config = new HashMap<>();
        tenant.createdAt = Instant.now();
        tenant.updatedAt = Instant.now();
        return tenant;
    }

    public static Tenant reconstitute(UUID id, String name, String code, boolean active,
                                      Map<String, Object> config, Instant createdAt,
                                      Instant updatedAt) {
        var tenant = new Tenant();
        tenant.id = id;
        tenant.name = name;
        tenant.code = code;
        tenant.active = active;
        tenant.config = config != null ? new HashMap<>(config) : new HashMap<>();
        tenant.createdAt = createdAt;
        tenant.updatedAt = updatedAt;
        return tenant;
    }

    public void update(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.name = name;
        this.updatedAt = Instant.now();
    }

    public void activate() {
        this.active = true;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.active = false;
        this.updatedAt = Instant.now();
    }

    public void updateConfig(Map<String, Object> config) {
        this.config = config != null ? new HashMap<>(config) : new HashMap<>();
        this.updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getCode() { return code; }
    public boolean isActive() { return active; }
    public Map<String, Object> getConfig() { return Collections.unmodifiableMap(config); }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
