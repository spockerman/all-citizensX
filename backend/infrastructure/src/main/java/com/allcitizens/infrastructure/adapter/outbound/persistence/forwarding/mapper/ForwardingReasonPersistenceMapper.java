package com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.mapper;

import com.allcitizens.domain.forwarding.ForwardingReason;
import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.entity.ForwardingReasonJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ForwardingReasonPersistenceMapper {

    public ForwardingReason toDomain(ForwardingReasonJpaEntity e) {
        return ForwardingReason.reconstitute(e.getId(), e.getName(), e.getType());
    }

    public ForwardingReasonJpaEntity toEntity(ForwardingReason d) {
        var e = new ForwardingReasonJpaEntity();
        e.setId(d.getId());
        e.setName(d.getName());
        e.setType(d.getType());
        return e;
    }
}
