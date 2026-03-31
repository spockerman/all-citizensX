package com.allcitizens.application.subject.command;

import java.util.UUID;

public record UpdateSubjectCommand(
    String name,
    UUID departmentId,
    Boolean active,
    Boolean visibleWeb,
    Boolean visibleApp
) {
}
