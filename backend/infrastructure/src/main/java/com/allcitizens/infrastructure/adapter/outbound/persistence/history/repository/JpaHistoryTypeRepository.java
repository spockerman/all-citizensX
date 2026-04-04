package com.allcitizens.infrastructure.adapter.outbound.persistence.history.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.history.entity.HistoryTypeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaHistoryTypeRepository extends JpaRepository<HistoryTypeJpaEntity, UUID> {
}
