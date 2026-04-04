package com.allcitizens.application.forwarding.command;

import java.util.UUID;

public record CreateRedistributionCommand(
        UUID forwardingId,
        UUID targetDepartmentId,
        UUID userId,
        String notes
) {
}
