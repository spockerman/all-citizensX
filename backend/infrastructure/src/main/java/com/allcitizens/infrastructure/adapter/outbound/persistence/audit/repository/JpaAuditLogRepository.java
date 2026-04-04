package com.allcitizens.infrastructure.adapter.outbound.persistence.audit.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.audit.entity.AuditLogJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.UUID;

public interface JpaAuditLogRepository extends JpaRepository<AuditLogJpaEntity, UUID> {

    @Query("""
            SELECT a FROM AuditLogJpaEntity a
            WHERE (:from IS NULL OR a.occurredAt >= :from)
              AND (:to IS NULL OR a.occurredAt <= :to)
            """)
    Page<AuditLogJpaEntity> findFiltered(
            @Param("from") Instant from,
            @Param("to") Instant to,
            Pageable pageable);
}
