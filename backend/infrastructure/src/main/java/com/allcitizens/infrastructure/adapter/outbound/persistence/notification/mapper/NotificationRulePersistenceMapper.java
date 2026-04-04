package com.allcitizens.infrastructure.adapter.outbound.persistence.notification.mapper;

import com.allcitizens.domain.notification.NotificationChannel;
import com.allcitizens.domain.notification.NotificationRule;
import com.allcitizens.infrastructure.adapter.outbound.persistence.notification.entity.NotificationChannelJpa;
import com.allcitizens.infrastructure.adapter.outbound.persistence.notification.entity.NotificationRuleJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificationRulePersistenceMapper {

    public NotificationRule toDomain(NotificationRuleJpaEntity entity) {
        return NotificationRule.reconstitute(
                entity.getId(),
                entity.getServiceId(),
                entity.getEvent(),
                entity.getChannel() != null ? NotificationChannel.valueOf(entity.getChannel().name()) : null,
                entity.getTemplate(),
                entity.isActive());
    }

    public NotificationRuleJpaEntity toEntity(NotificationRule domain) {
        var entity = new NotificationRuleJpaEntity();
        entity.setId(domain.getId());
        entity.setServiceId(domain.getServiceId());
        entity.setEvent(domain.getEvent());
        entity.setChannel(domain.getChannel() != null
                ? NotificationChannelJpa.valueOf(domain.getChannel().name()) : null);
        entity.setTemplate(domain.getTemplate());
        entity.setActive(domain.isActive());
        return entity;
    }
}
