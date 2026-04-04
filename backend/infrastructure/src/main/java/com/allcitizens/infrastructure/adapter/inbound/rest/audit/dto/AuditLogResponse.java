package com.allcitizens.infrastructure.adapter.inbound.rest.audit.dto;

import java.time.Instant;
import java.util.UUID;

public record AuditLogResponse(
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
}
