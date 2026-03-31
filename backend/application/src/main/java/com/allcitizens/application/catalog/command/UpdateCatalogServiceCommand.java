package com.allcitizens.application.catalog.command;

import java.util.UUID;

public record UpdateCatalogServiceCommand(
    String displayName,
    String description,
    Integer slaDays,
    String defaultPriority,
    Boolean allowsAnonymous,
    Boolean allowsMultiForward,
    Boolean visibleWeb,
    Boolean visibleApp,
    Boolean active,
    UUID departmentId
) {
}
