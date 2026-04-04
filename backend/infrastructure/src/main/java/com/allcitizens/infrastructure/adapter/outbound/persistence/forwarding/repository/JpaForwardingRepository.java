package com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.entity.ForwardingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaForwardingRepository extends JpaRepository<ForwardingJpaEntity, UUID> {

    List<ForwardingJpaEntity> findAllByRequestIdOrderByCreatedAtDesc(UUID requestId);
}
