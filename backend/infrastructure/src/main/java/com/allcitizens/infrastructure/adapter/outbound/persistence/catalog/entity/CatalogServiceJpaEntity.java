package com.allcitizens.infrastructure.adapter.outbound.persistence.catalog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "service")
public class CatalogServiceJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "subject_id", nullable = false)
    private UUID subjectId;

    @Column(name = "subdivision_id", nullable = false)
    private UUID subdivisionId;

    @Column(name = "department_id")
    private UUID departmentId;

    @Column(name = "display_name", length = 300)
    private String displayName;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "sla_days", nullable = false)
    private int slaDays;

    @Column(name = "default_priority")
    private String defaultPriority;

    @Column(name = "allows_anonymous", nullable = false)
    private boolean allowsAnonymous;

    @Column(name = "allows_multi_forward", nullable = false)
    private boolean allowsMultiForward;

    @Column(name = "visible_web", nullable = false)
    private boolean visibleWeb;

    @Column(name = "visible_app", nullable = false)
    private boolean visibleApp;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "dynamic_fields", columnDefinition = "jsonb")
    private String dynamicFields;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getTenantId() { return tenantId; }
    public void setTenantId(UUID tenantId) { this.tenantId = tenantId; }

    public UUID getSubjectId() { return subjectId; }
    public void setSubjectId(UUID subjectId) { this.subjectId = subjectId; }

    public UUID getSubdivisionId() { return subdivisionId; }
    public void setSubdivisionId(UUID subdivisionId) { this.subdivisionId = subdivisionId; }

    public UUID getDepartmentId() { return departmentId; }
    public void setDepartmentId(UUID departmentId) { this.departmentId = departmentId; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getSlaDays() { return slaDays; }
    public void setSlaDays(int slaDays) { this.slaDays = slaDays; }

    public String getDefaultPriority() { return defaultPriority; }
    public void setDefaultPriority(String defaultPriority) { this.defaultPriority = defaultPriority; }

    public boolean isAllowsAnonymous() { return allowsAnonymous; }
    public void setAllowsAnonymous(boolean allowsAnonymous) { this.allowsAnonymous = allowsAnonymous; }

    public boolean isAllowsMultiForward() { return allowsMultiForward; }
    public void setAllowsMultiForward(boolean allowsMultiForward) { this.allowsMultiForward = allowsMultiForward; }

    public boolean isVisibleWeb() { return visibleWeb; }
    public void setVisibleWeb(boolean visibleWeb) { this.visibleWeb = visibleWeb; }

    public boolean isVisibleApp() { return visibleApp; }
    public void setVisibleApp(boolean visibleApp) { this.visibleApp = visibleApp; }

    public String getDynamicFields() { return dynamicFields; }
    public void setDynamicFields(String dynamicFields) { this.dynamicFields = dynamicFields; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
