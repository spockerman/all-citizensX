package com.allcitizens.infrastructure.adapter.outbound.persistence.subdivision.mapper;

import com.allcitizens.domain.subdivision.Subdivision;
import com.allcitizens.infrastructure.adapter.outbound.persistence.subdivision.entity.SubdivisionJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SubdivisionPersistenceMapper {

    public Subdivision toDomain(SubdivisionJpaEntity entity) {
        return Subdivision.reconstitute(
            entity.getId(),
            entity.getName(),
            entity.isActive()
        );
    }

    public SubdivisionJpaEntity toEntity(Subdivision subdivision) {
        var entity = new SubdivisionJpaEntity();
        entity.setId(subdivision.getId());
        entity.setName(subdivision.getName());
        entity.setActive(subdivision.isActive());
        return entity;
    }
}
