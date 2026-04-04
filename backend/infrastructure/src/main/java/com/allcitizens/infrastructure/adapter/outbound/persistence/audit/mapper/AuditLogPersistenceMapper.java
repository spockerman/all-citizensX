package com.allcitizens.infrastructure.adapter.outbound.persistence.audit.mapper;

import com.allcitizens.domain.audit.AuditLogEntry;
import com.allcitizens.infrastructure.adapter.outbound.persistence.audit.entity.AuditLogJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class AuditLogPersistenceMapper {

    public AuditLogJpaEntity toEntity(AuditLogEntry entry) {
        var e = new AuditLogJpaEntity();
        e.setId(entry.getId());
        e.setOccurredAt(entry.getOccurredAt());
        e.setActorSubject(entry.getActorSubject());
        e.setActorUsername(entry.getActorUsername());
        e.setActorRoles(entry.getActorRoles());
        e.setHttpMethod(entry.getHttpMethod());
        e.setRequestPath(entry.getRequestPath());
        e.setClientIp(entry.getClientIp());
        e.setResponseStatus(entry.getResponseStatus());
        e.setCorrelationId(entry.getCorrelationId());
        return e;
    }

    public AuditLogEntry toDomain(AuditLogJpaEntity e) {
        return AuditLogEntry.reconstitute(
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
