package com.allcitizens.infrastructure.adapter.outbound.persistence.tenant.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.tenant.entity.TenantJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface JpaTenantRepository extends JpaRepository<TenantJpaEntity, UUID> {

    Optional<TenantJpaEntity> findByCode(String code);

    boolean existsByCode(String code);
}
