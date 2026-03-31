package com.allcitizens.infrastructure.adapter.inbound.rest.department.dto;

import java.util.UUID;

public record UpdateDepartmentRequest(
    String name,
    String abbreviation,
    String email,
    Boolean canRespond,
    Boolean active,
    UUID parentId,
    int displayOrder
) {
}
