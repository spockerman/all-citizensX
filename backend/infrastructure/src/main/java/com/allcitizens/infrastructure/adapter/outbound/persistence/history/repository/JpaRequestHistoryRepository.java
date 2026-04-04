package com.allcitizens.infrastructure.adapter.outbound.persistence.history.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.history.entity.RequestHistoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaRequestHistoryRepository extends JpaRepository<RequestHistoryJpaEntity, UUID> {

    List<RequestHistoryJpaEntity> findAllByRequestIdOrderByCreatedAtDesc(UUID requestId);
}
