package com.allcitizens.infrastructure.adapter.outbound.persistence.subject.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.subject.entity.SubjectJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaSubjectRepository extends JpaRepository<SubjectJpaEntity, UUID> {

    List<SubjectJpaEntity> findAllByTenantId(UUID tenantId);
}
