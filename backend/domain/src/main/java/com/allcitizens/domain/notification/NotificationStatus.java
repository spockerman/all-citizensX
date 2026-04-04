package com.allcitizens.domain.notification;

/**
 * Matches PostgreSQL {@code notification_status_type}.
 */
public enum NotificationStatus {
    PENDING,
    SENT,
    FAILED,
    READ
}
