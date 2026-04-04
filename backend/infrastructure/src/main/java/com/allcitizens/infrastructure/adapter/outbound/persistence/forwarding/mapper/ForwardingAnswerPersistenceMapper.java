package com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.mapper;

import com.allcitizens.domain.forwarding.ForwardingAnswer;
import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.entity.ForwardingAnswerJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ForwardingAnswerPersistenceMapper {

    public ForwardingAnswer toDomain(ForwardingAnswerJpaEntity e) {
        return ForwardingAnswer.reconstitute(
                e.getId(),
                e.getForwardingId(),
                e.getDepartmentId(),
                e.getUserId(),
                e.getReasonId(),
                e.getResponse(),
                e.getCreatedAt()
        );
    }

    public ForwardingAnswerJpaEntity toEntity(ForwardingAnswer d) {
        var e = new ForwardingAnswerJpaEntity();
        e.setId(d.getId());
        e.setForwardingId(d.getForwardingId());
        e.setDepartmentId(d.getDepartmentId());
        e.setUserId(d.getUserId());
        e.setReasonId(d.getReasonId());
        e.setResponse(d.getResponse());
        e.setCreatedAt(d.getCreatedAt());
        return e;
    }
}
