package com.allcitizens.application.forwarding.command;

import java.time.LocalDate;
import java.util.UUID;

public record CreateForwardingCommand(
        UUID requestId,
        UUID targetDepartmentId,
        UUID sourceDepartmentId,
        UUID reasonId,
        UUID userId,
        String notes,
        LocalDate dueDate
) {
}
