package com.allcitizens.infrastructure.adapter.inbound.rest.request.dto;

import java.util.UUID;

public record UpdateServiceRequestRequest(
        String description,
        String internalNote,
        UUID personId,
        UUID addressId,
        String priority
) {
}
