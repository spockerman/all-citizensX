package com.allcitizens.infrastructure.adapter.outbound.persistence.notification.repository;

import com.allcitizens.infrastructure.adapter.outbound.persistence.notification.entity.NotificationRuleJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaNotificationRuleRepository extends JpaRepository<NotificationRuleJpaEntity, UUID> {

    List<NotificationRuleJpaEntity> findByServiceIdOrderByEventAscChannelAsc(UUID serviceId);
}
