package com.allcitizens.infrastructure.adapter.outbound.persistence.notification.entity;

/**
 * Matches PostgreSQL {@code notification_channel_type}.
 */
public enum NotificationChannelJpa {
    PUSH,
    EMAIL,
    SMS,
    WHATSAPP,
    IN_APP
}
