package com.allcitizens.application.catalog.command;

import java.util.UUID;

public record CreateCatalogServiceCommand(
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
    boolean visibleApp
) {
}
