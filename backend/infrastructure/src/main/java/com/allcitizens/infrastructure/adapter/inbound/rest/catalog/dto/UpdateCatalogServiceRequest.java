package com.allcitizens.infrastructure.adapter.inbound.rest.catalog.dto;

import java.util.UUID;

public record UpdateCatalogServiceRequest(
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
