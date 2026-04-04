package com.allcitizens.domain.audit;

import java.time.Instant;
import java.util.UUID;

public final class AuditLogEntry {

    private static final int MAX_PATH = 2000;
    private static final int MAX_SUBJECT = 255;
    private static final int MAX_USERNAME = 255;
    private static final int MAX_ROLES = 500;
    private static final int MAX_IP = 64;
    private static final int MAX_CORRELATION = 64;

    private UUID id;
    private Instant occurredAt;
    private String actorSubject;
    private String actorUsername;
    private String actorRoles;
    private String httpMethod;
    private String requestPath;
    private String clientIp;
    private int responseStatus;
    private String correlationId;

    private AuditLogEntry() {
    }

    public static AuditLogEntry create(
            Instant occurredAt,
            String actorSubject,
            String actorUsername,
            String actorRoles,
            String httpMethod,
            String requestPath,
            String clientIp,
            int responseStatus,
            String correlationId) {
        if (httpMethod == null || httpMethod.isBlank()) {
            throw new IllegalArgumentException("httpMethod must not be blank");
        }
        if (requestPath == null || requestPath.isBlank()) {
            throw new IllegalArgumentException("requestPath must not be blank");
        }
        var e = new AuditLogEntry();
        e.id = UUID.randomUUID();
        e.occurredAt = occurredAt != null ? occurredAt : Instant.now();
        e.actorSubject = truncate(actorSubject, MAX_SUBJECT);
        e.actorUsername = truncate(actorUsername, MAX_USERNAME);
        e.actorRoles = truncate(actorRoles, MAX_ROLES);
        e.httpMethod = httpMethod;
        e.requestPath = truncate(requestPath, MAX_PATH);
        e.clientIp = truncate(clientIp, MAX_IP);
        e.responseStatus = responseStatus;
        e.correlationId = truncate(correlationId, MAX_CORRELATION);
        return e;
    }

    public static AuditLogEntry reconstitute(
            UUID id,
            Instant occurredAt,
            String actorSubject,
            String actorUsername,
            String actorRoles,
            String httpMethod,
            String requestPath,
            String clientIp,
            int responseStatus,
            String correlationId) {
        var e = new AuditLogEntry();
        e.id = id;
        e.occurredAt = occurredAt;
        e.actorSubject = actorSubject;
        e.actorUsername = actorUsername;
        e.actorRoles = actorRoles;
        e.httpMethod = httpMethod;
        e.requestPath = requestPath;
        e.clientIp = clientIp;
        e.responseStatus = responseStatus;
        e.correlationId = correlationId;
        return e;
    }

    private static String truncate(String value, int max) {
        if (value == null) {
            return null;
        }
        if (value.length() <= max) {
            return value;
        }
        return value.substring(0, max);
    }

    public UUID getId() {
        return id;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public String getActorSubject() {
        return actorSubject;
    }

    public String getActorUsername() {
        return actorUsername;
    }

    public String getActorRoles() {
        return actorRoles;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public String getClientIp() {
        return clientIp;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public String getCorrelationId() {
        return correlationId;
    }
}
