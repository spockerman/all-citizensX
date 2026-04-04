package com.allcitizens.infrastructure.adapter.inbound.rest.audit.mapper;

import com.allcitizens.application.audit.result.AuditLogResult;
import com.allcitizens.infrastructure.adapter.inbound.rest.audit.dto.AuditLogResponse;
import org.springframework.stereotype.Component;

@Component
public class AuditLogRestMapper {

    public AuditLogResponse toResponse(AuditLogResult result) {
        return new AuditLogResponse(
                result.id(),
                result.occurredAt(),
                result.actorSubject(),
                result.actorUsername(),
                result.actorRoles(),
                result.httpMethod(),
                result.requestPath(),
                result.clientIp(),
                result.responseStatus(),
                result.correlationId());
    }
}
