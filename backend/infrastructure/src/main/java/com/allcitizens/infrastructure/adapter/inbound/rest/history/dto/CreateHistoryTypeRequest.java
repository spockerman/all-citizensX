package com.allcitizens.infrastructure.adapter.inbound.rest.history.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateHistoryTypeRequest(@NotBlank String name) {
}
