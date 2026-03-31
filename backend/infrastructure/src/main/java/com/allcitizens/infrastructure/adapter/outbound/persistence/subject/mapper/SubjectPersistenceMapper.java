package com.allcitizens.infrastructure.adapter.outbound.persistence.subject.mapper;

import com.allcitizens.domain.subject.Subject;
import com.allcitizens.infrastructure.adapter.outbound.persistence.subject.entity.SubjectJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SubjectPersistenceMapper {

    public Subject toDomain(SubjectJpaEntity entity) {
        return Subject.reconstitute(
            entity.getId(),
            entity.getTenantId(),
            entity.getDepartmentId(),
            entity.getName(),
            entity.isActive(),
            entity.isVisibleWeb(),
            entity.isVisibleApp(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public SubjectJpaEntity toEntity(Subject subject) {
        var entity = new SubjectJpaEntity();
        entity.setId(subject.getId());
        entity.setTenantId(subject.getTenantId());
        entity.setDepartmentId(subject.getDepartmentId());
        entity.setName(subject.getName());
        entity.setActive(subject.isActive());
        entity.setVisibleWeb(subject.isVisibleWeb());
        entity.setVisibleApp(subject.isVisibleApp());
        entity.setCreatedAt(subject.getCreatedAt());
        entity.setUpdatedAt(subject.getUpdatedAt());
        return entity;
    }
}
