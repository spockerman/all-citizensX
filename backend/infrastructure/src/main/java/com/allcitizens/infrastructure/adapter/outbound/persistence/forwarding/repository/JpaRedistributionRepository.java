package com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.entity.RedistributionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaRedistributionRepository extends JpaRepository<RedistributionJpaEntity, UUID> {

    List<RedistributionJpaEntity> findAllByForwardingIdOrderByCreatedAtAsc(UUID forwardingId);
}
