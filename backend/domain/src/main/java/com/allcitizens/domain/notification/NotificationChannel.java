package com.allcitizens.domain.notification;

/**
 * Matches PostgreSQL {@code notification_channel_type}.
 */
public enum NotificationChannel {
    PUSH,
    EMAIL,
    SMS,
    WHATSAPP,
    IN_APP
}
