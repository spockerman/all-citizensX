package com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "forwarding")
public class ForwardingJpaEntity {

    @Id
    private UUID id;

    @Column(name = "request_id", nullable = false)
    private UUID requestId;

    @Column(name = "target_department_id", nullable = false)
    private UUID targetDepartmentId;

    @Column(name = "source_department_id")
    private UUID sourceDepartmentId;

    @Column(name = "reason_id")
    private UUID reasonId;

    @Column(name = "user_id")
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "status", nullable = false)
    private ForwardingStatusJpa status;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "read", nullable = false)
    private boolean read;

    @Column(name = "read_at")
    private Instant readAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "answered_at")
    private Instant answeredAt;

    public enum ForwardingStatusJpa {
        PENDING,
        IN_REVIEW,
        ANSWERED,
        RETURNED,
        REDISTRIBUTED,
        COMPLETED
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public void setRequestId(UUID requestId) {
        this.requestId = requestId;
    }

    public UUID getTargetDepartmentId() {
        return targetDepartmentId;
    }

    public void setTargetDepartmentId(UUID targetDepartmentId) {
        this.targetDepartmentId = targetDepartmentId;
    }

    public UUID getSourceDepartmentId() {
        return sourceDepartmentId;
    }

    public void setSourceDepartmentId(UUID sourceDepartmentId) {
        this.sourceDepartmentId = sourceDepartmentId;
    }

    public UUID getReasonId() {
        return reasonId;
    }

    public void setReasonId(UUID reasonId) {
        this.reasonId = reasonId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public ForwardingStatusJpa getStatus() {
        return status;
    }

    public void setStatus(ForwardingStatusJpa status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Instant getReadAt() {
        return readAt;
    }

    public void setReadAt(Instant readAt) {
        this.readAt = readAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(Instant answeredAt) {
        this.answeredAt = answeredAt;
    }
}
