package com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        UUID tenantId,
        UUID requestId,
        UUID recipientId,
        String channel,
        String title,
        String message,
        String status,
        Map<String, Object> extraData,
        Instant createdAt,
        Instant sentAt,
        Instant readAt) {
}
