package com.allcitizens.domain.forwarding;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Domain model for table {@code forwarding_response}.
 */
public class ForwardingAnswer {

    private UUID id;
    private UUID forwardingId;
    private UUID departmentId;
    private UUID userId;
    private UUID reasonId;
    private String response;
    private Instant createdAt;

    private ForwardingAnswer() {
    }

    public static ForwardingAnswer create(UUID forwardingId, UUID departmentId, UUID userId,
                                          UUID reasonId, String response) {
        Objects.requireNonNull(forwardingId, "forwardingId must not be null");
        Objects.requireNonNull(departmentId, "departmentId must not be null");
        Objects.requireNonNull(response, "response must not be null");

        var a = new ForwardingAnswer();
        a.id = UUID.randomUUID();
        a.forwardingId = forwardingId;
        a.departmentId = departmentId;
        a.userId = userId;
        a.reasonId = reasonId;
        a.response = response;
        a.createdAt = Instant.now();
        return a;
    }

    public static ForwardingAnswer reconstitute(UUID id, UUID forwardingId, UUID departmentId,
                                                UUID userId, UUID reasonId, String response,
                                                Instant createdAt) {
        var a = new ForwardingAnswer();
        a.id = id;
        a.forwardingId = forwardingId;
        a.departmentId = departmentId;
        a.userId = userId;
        a.reasonId = reasonId;
        a.response = response;
        a.createdAt = createdAt;
        return a;
    }

    public UUID getId() {
        return id;
    }

    public UUID getForwardingId() {
        return forwardingId;
    }

    public UUID getDepartmentId() {
        return departmentId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getReasonId() {
        return reasonId;
    }

    public String getResponse() {
        return response;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
