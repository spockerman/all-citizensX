package com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto;

import java.util.UUID;

public record NotificationRuleResponse(
        UUID id, UUID serviceId, String event, String channel, String template, boolean active) {
}
