package com.allcitizens.application.notification.result;

import com.allcitizens.domain.notification.Notification;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record NotificationResult(
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

    public static NotificationResult fromDomain(Notification n) {
        return new NotificationResult(
                n.getId(),
                n.getTenantId(),
                n.getRequestId(),
                n.getRecipientId(),
                n.getChannel().name(),
                n.getTitle(),
                n.getMessage(),
                n.getStatus().name(),
                n.getExtraData(),
                n.getCreatedAt(),
                n.getSentAt(),
                n.getReadAt());
    }
}
