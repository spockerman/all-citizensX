package com.allcitizens.domain.notification;

import com.allcitizens.domain.exception.BusinessRuleException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Notification {

    private UUID id;
    private UUID tenantId;
    private UUID requestId;
    private UUID recipientId;
    private NotificationChannel channel;
    private String title;
    private String message;
    private NotificationStatus status;
    private Map<String, Object> extraData;
    private Instant createdAt;
    private Instant sentAt;
    private Instant readAt;

    private Notification() {
    }

    public static Notification create(
            UUID tenantId,
            UUID requestId,
            UUID recipientId,
            NotificationChannel channel,
            String title,
            String message,
            Map<String, Object> extraData) {
        Objects.requireNonNull(tenantId, "tenantId must not be null");
        Objects.requireNonNull(channel, "channel must not be null");
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message must not be blank");
        }

        var n = new Notification();
        n.id = UUID.randomUUID();
        n.tenantId = tenantId;
        n.requestId = requestId;
        n.recipientId = recipientId;
        n.channel = channel;
        n.title = title;
        n.message = message;
        n.status = NotificationStatus.PENDING;
        n.extraData = extraData != null ? new HashMap<>(extraData) : new HashMap<>();
        n.createdAt = Instant.now();
        return n;
    }

    public static Notification reconstitute(
            UUID id,
            UUID tenantId,
            UUID requestId,
            UUID recipientId,
            NotificationChannel channel,
            String title,
            String message,
            NotificationStatus status,
            Map<String, Object> extraData,
            Instant createdAt,
            Instant sentAt,
            Instant readAt) {
        var n = new Notification();
        n.id = id;
        n.tenantId = tenantId;
        n.requestId = requestId;
        n.recipientId = recipientId;
        n.channel = channel;
        n.title = title;
        n.message = message;
        n.status = status;
        n.extraData = extraData != null ? new HashMap<>(extraData) : new HashMap<>();
        n.createdAt = createdAt;
        n.sentAt = sentAt;
        n.readAt = readAt;
        return n;
    }

    public void markSent(Instant sentAt) {
        Objects.requireNonNull(sentAt, "sentAt must not be null");
        if (status != NotificationStatus.PENDING) {
            throw new BusinessRuleException("Only PENDING notification can be marked SENT");
        }
        this.status = NotificationStatus.SENT;
        this.sentAt = sentAt;
    }

    public void markFailed() {
        if (status != NotificationStatus.PENDING) {
            throw new BusinessRuleException("Only PENDING notification can be marked FAILED");
        }
        this.status = NotificationStatus.FAILED;
    }

    public void markRead(Instant readAt) {
        Objects.requireNonNull(readAt, "readAt must not be null");
        if (status != NotificationStatus.SENT) {
            throw new BusinessRuleException("Only SENT notification can be marked READ");
        }
        this.status = NotificationStatus.READ;
        this.readAt = readAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public UUID getRecipientId() {
        return recipientId;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public Map<String, Object> getExtraData() {
        return Map.copyOf(extraData);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public Instant getReadAt() {
        return readAt;
    }
}
