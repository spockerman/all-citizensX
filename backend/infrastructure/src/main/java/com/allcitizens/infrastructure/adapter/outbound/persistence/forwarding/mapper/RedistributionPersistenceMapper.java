package com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.mapper;

import com.allcitizens.domain.forwarding.ForwardingStatus;
import com.allcitizens.domain.forwarding.Redistribution;
import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.entity.ForwardingJpaEntity;
import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.entity.RedistributionJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RedistributionPersistenceMapper {

    public Redistribution toDomain(RedistributionJpaEntity e) {
        return Redistribution.reconstitute(
                e.getId(),
                e.getForwardingId(),
                e.getTargetDepartmentId(),
                e.getUserId(),
                ForwardingStatus.valueOf(e.getStatus().name()),
                e.isRead(),
                e.getNotes(),
                e.getCreatedAt()
        );
    }

    public RedistributionJpaEntity toEntity(Redistribution d) {
        var e = new RedistributionJpaEntity();
        e.setId(d.getId());
        e.setForwardingId(d.getForwardingId());
        e.setTargetDepartmentId(d.getTargetDepartmentId());
        e.setUserId(d.getUserId());
        e.setStatus(ForwardingJpaEntity.ForwardingStatusJpa.valueOf(d.getStatus().name()));
        e.setRead(d.isRead());
        e.setNotes(d.getNotes());
        e.setCreatedAt(d.getCreatedAt());
        return e;
    }
}
