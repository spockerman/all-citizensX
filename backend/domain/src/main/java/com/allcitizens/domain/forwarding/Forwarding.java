package com.allcitizens.domain.forwarding;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Forwarding {

    private UUID id;
    private UUID requestId;
    private UUID targetDepartmentId;
    private UUID sourceDepartmentId;
    private UUID reasonId;
    private UUID userId;
    private ForwardingStatus status;
    private String notes;
    private LocalDate dueDate;
    private boolean read;
    private Instant readAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant answeredAt;

    private Forwarding() {
    }

    public static Forwarding create(UUID requestId, UUID targetDepartmentId,
                                    UUID sourceDepartmentId, UUID reasonId, UUID userId,
                                    String notes, LocalDate dueDate) {
        Objects.requireNonNull(requestId, "requestId must not be null");
        Objects.requireNonNull(targetDepartmentId, "targetDepartmentId must not be null");

        var f = new Forwarding();
        f.id = UUID.randomUUID();
        f.requestId = requestId;
        f.targetDepartmentId = targetDepartmentId;
        f.sourceDepartmentId = sourceDepartmentId;
        f.reasonId = reasonId;
        f.userId = userId;
        f.status = ForwardingStatus.PENDING;
        f.notes = notes;
        f.dueDate = dueDate;
        f.read = false;
        f.createdAt = Instant.now();
        f.updatedAt = Instant.now();
        return f;
    }

    public static Forwarding reconstitute(UUID id, UUID requestId, UUID targetDepartmentId,
                                         UUID sourceDepartmentId, UUID reasonId, UUID userId,
                                         ForwardingStatus status, String notes, LocalDate dueDate,
                                         boolean read, Instant readAt, Instant createdAt,
                                         Instant updatedAt, Instant answeredAt) {
        var f = new Forwarding();
        f.id = id;
        f.requestId = requestId;
        f.targetDepartmentId = targetDepartmentId;
        f.sourceDepartmentId = sourceDepartmentId;
        f.reasonId = reasonId;
        f.userId = userId;
        f.status = status;
        f.notes = notes;
        f.dueDate = dueDate;
        f.read = read;
        f.readAt = readAt;
        f.createdAt = createdAt;
        f.updatedAt = updatedAt;
        f.answeredAt = answeredAt;
        return f;
    }

    public void markRead(Instant at) {
        this.read = true;
        this.readAt = at;
        this.updatedAt = Instant.now();
    }

    public void setStatus(ForwardingStatus status) {
        this.status = Objects.requireNonNull(status);
        this.updatedAt = Instant.now();
    }

    public void setAnsweredAt(Instant answeredAt) {
        this.answeredAt = answeredAt;
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public UUID getTargetDepartmentId() {
        return targetDepartmentId;
    }

    public UUID getSourceDepartmentId() {
        return sourceDepartmentId;
    }

    public UUID getReasonId() {
        return reasonId;
    }

    public UUID getUserId() {
        return userId;
    }

    public ForwardingStatus getStatus() {
        return status;
    }

    public String getNotes() {
        return notes;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isRead() {
        return read;
    }

    public Instant getReadAt() {
        return readAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getAnsweredAt() {
        return answeredAt;
    }
}
