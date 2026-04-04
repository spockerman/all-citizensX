package com.allcitizens.application.notification.query;

import java.util.UUID;

public record ListNotificationsQuery(UUID tenantId, UUID requestId, int page, int size) {
}
