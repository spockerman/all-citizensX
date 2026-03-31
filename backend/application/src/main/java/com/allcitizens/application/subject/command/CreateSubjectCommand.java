package com.allcitizens.application.subject.command;

import java.util.UUID;

public record CreateSubjectCommand(
    UUID tenantId,
    UUID departmentId,
    String name,
    boolean visibleWeb,
    boolean visibleApp
) {
}
