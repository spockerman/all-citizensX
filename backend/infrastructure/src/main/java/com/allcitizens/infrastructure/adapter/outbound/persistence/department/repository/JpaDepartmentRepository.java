package com.allcitizens.infrastructure.adapter.outbound.persistence.department.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.department.entity.DepartmentJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface JpaDepartmentRepository extends JpaRepository<DepartmentJpaEntity, UUID> {

    List<DepartmentJpaEntity> findAllByTenantId(UUID tenantId);

    Page<DepartmentJpaEntity> findAllByTenantId(UUID tenantId, Pageable pageable);

    @Query("""
            SELECT d FROM DepartmentJpaEntity d
            WHERE d.tenantId = :tenantId
              AND (
                LOWER(d.name) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(COALESCE(d.abbreviation, '')) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(COALESCE(d.email, '')) LIKE LOWER(CONCAT('%', :q, '%'))
              )
            """)
    Page<DepartmentJpaEntity> searchByTenantId(
            @Param("tenantId") UUID tenantId,
            @Param("q") String q,
            Pageable pageable);

    List<DepartmentJpaEntity> findByTenantIdAndParentIdIsNull(UUID tenantId);
}
