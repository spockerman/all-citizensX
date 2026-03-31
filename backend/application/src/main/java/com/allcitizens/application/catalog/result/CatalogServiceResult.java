package com.allcitizens.application.catalog.result;

import com.allcitizens.domain.catalog.CatalogService;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record CatalogServiceResult(
    UUID id,
    UUID tenantId,
    UUID subjectId,
    UUID subdivisionId,
    UUID departmentId,
    String displayName,
    String description,
    int slaDays,
    String defaultPriority,
    boolean allowsAnonymous,
    boolean allowsMultiForward,
    boolean visibleWeb,
    boolean visibleApp,
    Map<String, Object> dynamicFields,
    boolean active,
    Instant createdAt,
    Instant updatedAt
) {

    public static CatalogServiceResult fromDomain(CatalogService catalogService) {
        Map<String, Object> fields = catalogService.getDynamicFields();
        Map<String, Object> dynamicFieldsCopy = fields == null ? null : new HashMap<>(fields);
        return new CatalogServiceResult(
            catalogService.getId(),
            catalogService.getTenantId(),
            catalogService.getSubjectId(),
            catalogService.getSubdivisionId(),
            catalogService.getDepartmentId(),
            catalogService.getDisplayName(),
            catalogService.getDescription(),
            catalogService.getSlaDays(),
            catalogService.getDefaultPriority().name(),
            catalogService.isAllowsAnonymous(),
            catalogService.isAllowsMultiForward(),
            catalogService.isVisibleWeb(),
            catalogService.isVisibleApp(),
            dynamicFieldsCopy,
            catalogService.isActive(),
            catalogService.getCreatedAt(),
            catalogService.getUpdatedAt()
        );
    }
}
