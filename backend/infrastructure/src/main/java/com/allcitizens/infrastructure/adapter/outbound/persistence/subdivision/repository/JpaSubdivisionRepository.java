package com.allcitizens.infrastructure.adapter.outbound.persistence.subdivision.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.subdivision.entity.SubdivisionJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface JpaSubdivisionRepository extends JpaRepository<SubdivisionJpaEntity, UUID> {

    Page<SubdivisionJpaEntity> findAll(Pageable pageable);

    @Query("SELECT s FROM SubdivisionJpaEntity s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :q, '%'))")
    Page<SubdivisionJpaEntity> searchByName(@Param("q") String q, Pageable pageable);
}
