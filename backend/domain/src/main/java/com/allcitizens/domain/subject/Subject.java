package com.allcitizens.domain.subject;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Subject {

    private UUID id;
    private UUID tenantId;
    private UUID departmentId;
    private String name;
    private boolean active;
    private boolean visibleWeb;
    private boolean visibleApp;
    private Instant createdAt;
    private Instant updatedAt;

    private Subject() {
    }

    public static Subject create(UUID tenantId, String name) {
        Objects.requireNonNull(tenantId, "tenantId must not be null");
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }

        var subject = new Subject();
        subject.id = UUID.randomUUID();
        subject.tenantId = tenantId;
        subject.name = name;
        subject.active = true;
        subject.visibleWeb = true;
        subject.visibleApp = true;
        subject.createdAt = Instant.now();
        subject.updatedAt = Instant.now();
        return subject;
    }

    public static Subject reconstitute(UUID id, UUID tenantId, UUID departmentId, String name,
                                       boolean active, boolean visibleWeb, boolean visibleApp,
                                       Instant createdAt, Instant updatedAt) {
        var subject = new Subject();
        subject.id = id;
        subject.tenantId = tenantId;
        subject.departmentId = departmentId;
        subject.name = name;
        subject.active = active;
        subject.visibleWeb = visibleWeb;
        subject.visibleApp = visibleApp;
        subject.createdAt = createdAt;
        subject.updatedAt = updatedAt;
        return subject;
    }

    public void update(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        this.name = name;
        this.updatedAt = Instant.now();
    }

    public void assignDepartment(UUID departmentId) {
        Objects.requireNonNull(departmentId, "departmentId must not be null");
        this.departmentId = departmentId;
        this.updatedAt = Instant.now();
    }

    public void removeDepartment() {
        this.departmentId = null;
        this.updatedAt = Instant.now();
    }

    public void setVisibility(boolean visibleWeb, boolean visibleApp) {
        this.visibleWeb = visibleWeb;
        this.visibleApp = visibleApp;
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

    public UUID getId() { return id; }
    public UUID getTenantId() { return tenantId; }
    public UUID getDepartmentId() { return departmentId; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
    public boolean isVisibleWeb() { return visibleWeb; }
    public boolean isVisibleApp() { return visibleApp; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
