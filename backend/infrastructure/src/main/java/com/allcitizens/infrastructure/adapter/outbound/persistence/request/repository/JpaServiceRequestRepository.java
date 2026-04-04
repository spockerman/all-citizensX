package com.allcitizens.infrastructure.adapter.outbound.persistence.request.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.request.entity.ServiceRequestJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaServiceRequestRepository extends JpaRepository<ServiceRequestJpaEntity, UUID> {

    List<ServiceRequestJpaEntity> findAllByTenantId(UUID tenantId);

    Page<ServiceRequestJpaEntity> findAllByTenantId(UUID tenantId, Pageable pageable);

    @Query("""
            SELECT s FROM ServiceRequestJpaEntity s
            WHERE s.tenantId = :tenantId
              AND (
                LOWER(s.protocol) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(COALESCE(s.description, '')) LIKE LOWER(CONCAT('%', :q, '%'))
              )
            """)
    Page<ServiceRequestJpaEntity> searchByTenantId(
            @Param("tenantId") UUID tenantId,
            @Param("q") String q,
            Pageable pageable);

    Optional<ServiceRequestJpaEntity> findByTenantIdAndProtocol(UUID tenantId, String protocol);
}
