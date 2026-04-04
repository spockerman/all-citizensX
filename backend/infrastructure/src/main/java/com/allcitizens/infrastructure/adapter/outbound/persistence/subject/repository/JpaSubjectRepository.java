package com.allcitizens.infrastructure.adapter.outbound.persistence.subject.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.subject.entity.SubjectJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaSubjectRepository extends JpaRepository<SubjectJpaEntity, UUID> {

    List<SubjectJpaEntity> findAllByTenantId(UUID tenantId);

    Page<SubjectJpaEntity> findAllByTenantId(UUID tenantId, Pageable pageable);

    @Query("""
            SELECT s FROM SubjectJpaEntity s
            WHERE s.tenantId = :tenantId
              AND LOWER(s.name) LIKE LOWER(CONCAT('%', :q, '%'))
            """)
    Page<SubjectJpaEntity> searchByTenantId(
            @Param("tenantId") UUID tenantId,
            @Param("q") String q,
            Pageable pageable);
}
