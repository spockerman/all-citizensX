package com.allcitizens.infrastructure.adapter.outbound.persistence.history.mapper;

import com.allcitizens.domain.history.HistoryType;
import com.allcitizens.infrastructure.adapter.outbound.persistence.history.entity.HistoryTypeJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class HistoryTypePersistenceMapper {

    public HistoryType toDomain(HistoryTypeJpaEntity e) {
        return HistoryType.reconstitute(e.getId(), e.getName());
    }

    public HistoryTypeJpaEntity toEntity(HistoryType d) {
        var e = new HistoryTypeJpaEntity();
        e.setId(d.getId());
        e.setName(d.getName());
        return e;
    }
}
