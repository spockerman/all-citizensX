package com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.entity.ForwardingReasonJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaForwardingReasonRepository extends JpaRepository<ForwardingReasonJpaEntity, UUID> {
}
