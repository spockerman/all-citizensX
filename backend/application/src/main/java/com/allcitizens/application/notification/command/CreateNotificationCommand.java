package com.allcitizens.application.notification.command;

import java.util.Map;
import java.util.UUID;

public record CreateNotificationCommand(
        UUID tenantId,
        UUID requestId,
        UUID recipientId,
        String channel,
        String title,
        String message,
        Map<String, Object> extraData) {
}
