package com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.dto;

import java.util.UUID;

public record SubdivisionResponse(
    UUID id,
    String name,
    boolean active
) {
}
