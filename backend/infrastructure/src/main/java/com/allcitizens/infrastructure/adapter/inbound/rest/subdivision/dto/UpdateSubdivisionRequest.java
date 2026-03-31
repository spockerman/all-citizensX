package com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.dto;

public record UpdateSubdivisionRequest(
    String name,
    Boolean active
) {
}
