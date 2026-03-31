package com.allcitizens.domain.department;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Department {

    private UUID id;
    private UUID tenantId;
    private UUID parentId;
    private String name;
    private String abbreviation;
    private String email;
    private boolean active;
    private boolean canRespond;
    private boolean isRoot;
    private String iconUrl;
    private int displayOrder;
    private Instant createdAt;
    private Instant updatedAt;

    private Department() {
    }

    public static Department create(UUID tenantId, String name, String abbreviation,
                                    String email, boolean canRespond, boolean isRoot,
                                    int displayOrder) {
        Objects.requireNonNull(tenantId, "tenantId must not be null");
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }

        var department = new Department();
        department.id = UUID.randomUUID();
        department.tenantId = tenantId;
        department.name = name;
        department.abbreviation = abbreviation;
        department.email = email;
        department.active = true;
        department.canRespond = canRespond;
        department.isRoot = isRoot;
        department.displayOrder = displayOrder;
        department.createdAt = Instant.now();
        department.updatedAt = Instant.now();
        return department;
    }

    public static Department reconstitute(UUID id, UUID tenantId, UUID parentId, String name,
                                          String abbreviation, String email, boolean active,
                                          boolean canRespond, boolean isRoot, String iconUrl,
                                          int displayOrder, Instant createdAt, Instant updatedAt) {
        var department = new Department();
        department.id = id;
        department.tenantId = tenantId;
        department.parentId = parentId;
        department.name = name;
        department.abbreviation = abbreviation;
        department.email = email;
        department.active = active;
        department.canRespond = canRespond;
        department.isRoot = isRoot;
        department.iconUrl = iconUrl;
        department.displayOrder = displayOrder;
        department.createdAt = createdAt;
        department.updatedAt = updatedAt;
        return department;
    }

    public void update(String name, String abbreviation, String email,
                       boolean canRespond, int displayOrder) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.name = name;
        this.abbreviation = abbreviation;
        this.email = email;
        this.canRespond = canRespond;
        this.displayOrder = displayOrder;
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

    public void assignParent(UUID parentId) {
        Objects.requireNonNull(parentId, "parentId must not be null");
        this.parentId = parentId;
        this.updatedAt = Instant.now();
    }

    public void removeParent() {
        this.parentId = null;
        this.updatedAt = Instant.now();
    }

    public UUID getId() { return id; }
    public UUID getTenantId() { return tenantId; }
    public UUID getParentId() { return parentId; }
    public String getName() { return name; }
    public String getAbbreviation() { return abbreviation; }
    public String getEmail() { return email; }
    public boolean isActive() { return active; }
    public boolean isCanRespond() { return canRespond; }
    public boolean isRoot() { return isRoot; }
    public String getIconUrl() { return iconUrl; }
    public int getDisplayOrder() { return displayOrder; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
