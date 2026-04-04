package com.allcitizens.application.audit.result;

import java.time.Instant;
import java.util.UUID;

public record AuditLogResult(
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

    public static AuditLogResult fromDomain(com.allcitizens.domain.audit.AuditLogEntry e) {
        return new AuditLogResult(
                e.getId(),
                e.getOccurredAt(),
                e.getActorSubject(),
                e.getActorUsername(),
                e.getActorRoles(),
                e.getHttpMethod(),
                e.getRequestPath(),
                e.getClientIp(),
                e.getResponseStatus(),
                e.getCorrelationId());
    }
}
