package com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddForwardingAnswerRequest(
        @NotNull UUID departmentId,
        UUID userId,
        UUID reasonId,
        @NotBlank String response
) {
}
