package com.allcitizens.infrastructure.adapter.outbound.persistence.notification.entity;

/**
 * Matches PostgreSQL {@code notification_status_type}.
 */
public enum NotificationStatusJpa {
    PENDING,
    SENT,
    FAILED,
    READ
}
