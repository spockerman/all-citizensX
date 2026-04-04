package com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateNotificationRuleRequest(
        @NotNull UUID serviceId,
        @NotBlank String event,
        @NotBlank String channel,
        @NotBlank String template) {
}
