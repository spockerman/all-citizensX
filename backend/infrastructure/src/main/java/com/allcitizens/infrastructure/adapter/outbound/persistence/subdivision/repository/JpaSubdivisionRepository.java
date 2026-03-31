package com.allcitizens.infrastructure.adapter.outbound.persistence.subdivision.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.subdivision.entity.SubdivisionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaSubdivisionRepository extends JpaRepository<SubdivisionJpaEntity, UUID> {
}
