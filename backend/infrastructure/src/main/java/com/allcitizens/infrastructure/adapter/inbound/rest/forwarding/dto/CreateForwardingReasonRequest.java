package com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateForwardingReasonRequest(
        @NotBlank String name,
        @NotBlank String type
) {
}
