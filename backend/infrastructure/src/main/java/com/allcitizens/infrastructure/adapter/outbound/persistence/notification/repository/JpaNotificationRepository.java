package com.allcitizens.infrastructure.adapter.outbound.persistence.notification.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.notification.entity.NotificationJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaNotificationRepository extends JpaRepository<NotificationJpaEntity, UUID> {

    Page<NotificationJpaEntity> findByTenantIdOrderByCreatedAtDesc(UUID tenantId, Pageable pageable);

    Page<NotificationJpaEntity> findByTenantIdAndRequestIdOrderByCreatedAtDesc(
            UUID tenantId, UUID requestId, Pageable pageable);
}
