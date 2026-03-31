package com.allcitizens.infrastructure.adapter.outbound.persistence.request.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.request.entity.ServiceRequestJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaServiceRequestRepository extends JpaRepository<ServiceRequestJpaEntity, UUID> {

    List<ServiceRequestJpaEntity> findAllByTenantId(UUID tenantId);

    Optional<ServiceRequestJpaEntity> findByTenantIdAndProtocol(UUID tenantId, String protocol);
}
