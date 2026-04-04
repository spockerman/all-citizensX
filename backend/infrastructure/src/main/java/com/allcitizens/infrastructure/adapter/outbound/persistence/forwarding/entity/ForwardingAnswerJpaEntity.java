package com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "forwarding_response")
public class ForwardingAnswerJpaEntity {

    @Id
    private UUID id;

    @Column(name = "forwarding_id", nullable = false)
    private UUID forwardingId;

    @Column(name = "department_id", nullable = false)
    private UUID departmentId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "reason_id")
    private UUID reasonId;

    @Column(name = "response", nullable = false, columnDefinition = "TEXT")
    private String response;

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

    public UUID getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getReasonId() {
        return reasonId;
    }

    public void setReasonId(UUID reasonId) {
        this.reasonId = reasonId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
