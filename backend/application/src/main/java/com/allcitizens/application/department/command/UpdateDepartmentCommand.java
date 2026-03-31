package com.allcitizens.application.department.command;

import java.util.UUID;

public record UpdateDepartmentCommand(
    String name,
    String abbreviation,
    String email,
    Boolean canRespond,
    Boolean active,
    UUID parentId,
    int displayOrder
) {
}
