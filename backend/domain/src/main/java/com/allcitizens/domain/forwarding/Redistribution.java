package com.allcitizens.domain.forwarding;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Redistribution {

    private UUID id;
    private UUID forwardingId;
    private UUID targetDepartmentId;
    private UUID userId;
    private ForwardingStatus status;
    private boolean read;
    private String notes;
    private Instant createdAt;

    private Redistribution() {
    }

    public static Redistribution create(UUID forwardingId, UUID targetDepartmentId,
                                        UUID userId, String notes) {
        Objects.requireNonNull(forwardingId, "forwardingId must not be null");
        Objects.requireNonNull(targetDepartmentId, "targetDepartmentId must not be null");

        var r = new Redistribution();
        r.id = UUID.randomUUID();
        r.forwardingId = forwardingId;
        r.targetDepartmentId = targetDepartmentId;
        r.userId = userId;
        r.status = ForwardingStatus.PENDING;
        r.read = false;
        r.notes = notes;
        r.createdAt = Instant.now();
        return r;
    }

    public static Redistribution reconstitute(UUID id, UUID forwardingId, UUID targetDepartmentId,
                                              UUID userId, ForwardingStatus status, boolean read,
                                              String notes, Instant createdAt) {
        var r = new Redistribution();
        r.id = id;
        r.forwardingId = forwardingId;
        r.targetDepartmentId = targetDepartmentId;
        r.userId = userId;
        r.status = status;
        r.read = read;
        r.notes = notes;
        r.createdAt = createdAt;
        return r;
    }

    public void setStatus(ForwardingStatus status) {
        this.status = Objects.requireNonNull(status);
    }

    public void markRead() {
        this.read = true;
    }

    public UUID getId() {
        return id;
    }

    public UUID getForwardingId() {
        return forwardingId;
    }

    public UUID getTargetDepartmentId() {
        return targetDepartmentId;
    }

    public UUID getUserId() {
        return userId;
    }

    public ForwardingStatus getStatus() {
        return status;
    }

    public boolean isRead() {
        return read;
    }

    public String getNotes() {
        return notes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
