package com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.forwarding.entity.ForwardingAnswerJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaForwardingAnswerRepository extends JpaRepository<ForwardingAnswerJpaEntity, UUID> {

    List<ForwardingAnswerJpaEntity> findAllByForwardingIdOrderByCreatedAtAsc(UUID forwardingId);
}
