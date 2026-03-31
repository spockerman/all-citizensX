package com.allcitizens.domain.catalog;

import com.allcitizens.domain.request.Priority;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class CatalogService {

    private UUID id;
    private UUID tenantId;
    private UUID subjectId;
    private UUID subdivisionId;
    private UUID departmentId;
    private String displayName;
    private String description;
    private int slaDays;
    private Priority defaultPriority;
    private boolean allowsAnonymous;
    private boolean allowsMultiForward;
    private boolean visibleWeb;
    private boolean visibleApp;
    private Map<String, Object> dynamicFields;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

    private CatalogService() {
    }

    public static CatalogService create(UUID tenantId, UUID subjectId, UUID subdivisionId,
                                        String displayName, int slaDays) {
        Objects.requireNonNull(tenantId, "tenantId must not be null");
        Objects.requireNonNull(subjectId, "subjectId must not be null");
        Objects.requireNonNull(subdivisionId, "subdivisionId must not be null");
        if (slaDays < 0) {
            throw new IllegalArgumentException("slaDays must not be negative");
        }

        var catalogService = new CatalogService();
        catalogService.id = UUID.randomUUID();
        catalogService.tenantId = tenantId;
        catalogService.subjectId = subjectId;
        catalogService.subdivisionId = subdivisionId;
        catalogService.displayName = displayName;
        catalogService.description = null;
        catalogService.slaDays = slaDays;
        catalogService.defaultPriority = Priority.NORMAL;
        catalogService.allowsAnonymous = false;
        catalogService.allowsMultiForward = false;
        catalogService.visibleWeb = true;
        catalogService.visibleApp = true;
        catalogService.dynamicFields = new HashMap<>();
        catalogService.active = true;
        catalogService.createdAt = Instant.now();
        catalogService.updatedAt = Instant.now();
        return catalogService;
    }

    public static CatalogService reconstitute(UUID id, UUID tenantId, UUID subjectId, UUID subdivisionId,
                                              UUID departmentId, String displayName, String description,
                                              int slaDays, Priority defaultPriority, boolean allowsAnonymous,
                                              boolean allowsMultiForward, boolean visibleWeb, boolean visibleApp,
                                              Map<String, Object> dynamicFields, boolean active,
                                              Instant createdAt, Instant updatedAt) {
        var catalogService = new CatalogService();
        catalogService.id = id;
        catalogService.tenantId = tenantId;
        catalogService.subjectId = subjectId;
        catalogService.subdivisionId = subdivisionId;
        catalogService.departmentId = departmentId;
        catalogService.displayName = displayName;
        catalogService.description = description;
        catalogService.slaDays = slaDays;
        catalogService.defaultPriority = defaultPriority;
        catalogService.allowsAnonymous = allowsAnonymous;
        catalogService.allowsMultiForward = allowsMultiForward;
        catalogService.visibleWeb = visibleWeb;
        catalogService.visibleApp = visibleApp;
        catalogService.dynamicFields = dynamicFields;
        catalogService.active = active;
        catalogService.createdAt = createdAt;
        catalogService.updatedAt = updatedAt;
        return catalogService;
    }

    public void update(String displayName, String description, int slaDays, Priority defaultPriority,
                       boolean allowsAnonymous, boolean allowsMultiForward,
                       Map<String, Object> dynamicFields) {
        if (slaDays < 0) {
            throw new IllegalArgumentException("slaDays must not be negative");
        }
        Objects.requireNonNull(defaultPriority, "defaultPriority must not be null");
        this.displayName = displayName;
        this.description = description;
        this.slaDays = slaDays;
        this.defaultPriority = defaultPriority;
        this.allowsAnonymous = allowsAnonymous;
        this.allowsMultiForward = allowsMultiForward;
        this.dynamicFields = dynamicFields != null ? new HashMap<>(dynamicFields) : null;
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
    public UUID getSubjectId() { return subjectId; }
    public UUID getSubdivisionId() { return subdivisionId; }
    public UUID getDepartmentId() { return departmentId; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public int getSlaDays() { return slaDays; }
    public Priority getDefaultPriority() { return defaultPriority; }
    public boolean isAllowsAnonymous() { return allowsAnonymous; }
    public boolean isAllowsMultiForward() { return allowsMultiForward; }
    public boolean isVisibleWeb() { return visibleWeb; }
    public boolean isVisibleApp() { return visibleApp; }
    public Map<String, Object> getDynamicFields() { return dynamicFields; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
