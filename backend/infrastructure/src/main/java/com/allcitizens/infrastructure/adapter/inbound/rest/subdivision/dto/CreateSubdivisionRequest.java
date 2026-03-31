package com.allcitizens.infrastructure.adapter.inbound.rest.subdivision.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSubdivisionRequest(
    @NotBlank String name
) {
}
