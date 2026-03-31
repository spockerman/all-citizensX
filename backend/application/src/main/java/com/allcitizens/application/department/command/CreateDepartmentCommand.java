package com.allcitizens.application.department.command;

import java.util.UUID;

public record CreateDepartmentCommand(
    UUID tenantId,
    UUID parentId,
    String name,
    String abbreviation,
    String email,
    boolean canRespond,
    boolean isRoot,
    String iconUrl,
    int displayOrder
) {
}
