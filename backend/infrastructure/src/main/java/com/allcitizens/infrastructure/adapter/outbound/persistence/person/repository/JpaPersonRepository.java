package com.allcitizens.infrastructure.adapter.outbound.persistence.person.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.person.entity.PersonJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaPersonRepository extends JpaRepository<PersonJpaEntity, UUID> {

    List<PersonJpaEntity> findAllByTenantId(UUID tenantId);

    Optional<PersonJpaEntity> findByTenantIdAndEmail(UUID tenantId, String email);
}
