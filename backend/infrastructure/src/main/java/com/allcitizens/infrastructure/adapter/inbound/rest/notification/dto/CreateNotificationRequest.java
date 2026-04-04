package com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record CreateNotificationRequest(
        @NotNull UUID tenantId,
        UUID requestId,
        UUID recipientId,
        @NotBlank String channel,
        String title,
        @NotBlank String message,
        Map<String, Object> extraData) {
}
