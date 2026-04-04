package com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.mapper;

import com.allcitizens.domain.forwarding.Forwarding;
import com.allcitizens.domain.forwarding.ForwardingStatus;
import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.entity.ForwardingJpaEntity;
import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.entity.ForwardingJpaEntity.ForwardingStatusJpa;
import org.springframework.stereotype.Component;

@Component
public class ForwardingPersistenceMapper {

    public Forwarding toDomain(ForwardingJpaEntity e) {
        return Forwarding.reconstitute(
                e.getId(),
                e.getRequestId(),
                e.getTargetDepartmentId(),
                e.getSourceDepartmentId(),
                e.getReasonId(),
                e.getUserId(),
                mapStatus(e.getStatus()),
                e.getNotes(),
                e.getDueDate(),
                e.isRead(),
                e.getReadAt(),
                e.getCreatedAt(),
                e.getUpdatedAt(),
                e.getAnsweredAt()
        );
    }

    public ForwardingJpaEntity toEntity(Forwarding d) {
        var e = new ForwardingJpaEntity();
        e.setId(d.getId());
        e.setRequestId(d.getRequestId());
        e.setTargetDepartmentId(d.getTargetDepartmentId());
        e.setSourceDepartmentId(d.getSourceDepartmentId());
        e.setReasonId(d.getReasonId());
        e.setUserId(d.getUserId());
        e.setStatus(ForwardingStatusJpa.valueOf(d.getStatus().name()));
        e.setNotes(d.getNotes());
        e.setDueDate(d.getDueDate());
        e.setRead(d.isRead());
        e.setReadAt(d.getReadAt());
        e.setCreatedAt(d.getCreatedAt());
        e.setUpdatedAt(d.getUpdatedAt());
        e.setAnsweredAt(d.getAnsweredAt());
        return e;
    }

    private static ForwardingStatus mapStatus(ForwardingStatusJpa s) {
        return s == null ? null : ForwardingStatus.valueOf(s.name());
    }
}
