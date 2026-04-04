package com.allcitizens.infrastructure.adapter.outbound.persistence.attachment.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.attachment.entity.AttachmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaAttachmentRepository extends JpaRepository<AttachmentJpaEntity, UUID> {

    List<AttachmentJpaEntity> findAllByRequestIdOrderByCreatedAtDesc(UUID requestId);
}
