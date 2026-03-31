package com.allcitizens.application.request.command;

import com.allcitizens.domain.request.Priority;

import java.util.UUID;

public record UpdateServiceRequestCommand(
        String description,
        String internalNote,
        UUID personId,
        UUID addressId,
        Priority priority
) {
}
