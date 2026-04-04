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
import java.util.UUID;

@Entity
@Table(name = "redistribution")
public class RedistributionJpaEntity {

    @Id
    private UUID id;

    @Column(name = "forwarding_id", nullable = false)
    private UUID forwardingId;

    @Column(name = "target_department_id", nullable = false)
    private UUID targetDepartmentId;

    @Column(name = "user_id")
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    @Column(name = "status", nullable = false)
    private ForwardingJpaEntity.ForwardingStatusJpa status;

    @Column(name = "read", nullable = false)
    private boolean read;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getForwardingId() {
        return forwardingId;
    }

    public void setForwardingId(UUID forwardingId) {
        this.forwardingId = forwardingId;
    }

    public UUID getTargetDepartmentId() {
        return targetDepartmentId;
    }

    public void setTargetDepartmentId(UUID targetDepartmentId) {
        this.targetDepartmentId = targetDepartmentId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public ForwardingJpaEntity.ForwardingStatusJpa getStatus() {
        return status;
    }

    public void setStatus(ForwardingJpaEntity.ForwardingStatusJpa status) {
        this.status = status;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
