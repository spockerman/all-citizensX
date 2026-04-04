package com.allcitizens.infrastructure.adapter.outbound.persistence.person.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.person.entity.PersonJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaPersonRepository extends JpaRepository<PersonJpaEntity, UUID> {

    List<PersonJpaEntity> findAllByTenantId(UUID tenantId);

    Page<PersonJpaEntity> findAllByTenantId(UUID tenantId, Pageable pageable);

    @Query("""
            SELECT DISTINCT p FROM PersonJpaEntity p
            LEFT JOIN IndividualJpaEntity i ON i.personId = p.id
            WHERE p.tenantId = :tenantId
              AND (
                LOWER(COALESCE(p.email, '')) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(COALESCE(i.fullName, '')) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(COALESCE(i.taxId, '')) LIKE LOWER(CONCAT('%', :q, '%'))
              )
            """)
    Page<PersonJpaEntity> searchByTenantId(
            @Param("tenantId") UUID tenantId,
            @Param("q") String q,
            Pageable pageable);

    Optional<PersonJpaEntity> findByTenantIdAndEmail(UUID tenantId, String email);
}
