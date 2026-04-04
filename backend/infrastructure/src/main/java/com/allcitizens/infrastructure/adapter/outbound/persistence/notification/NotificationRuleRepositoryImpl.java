package com.allcitizens.infrastructure.adapter.outbound.persistence.notification;

import com.allcitizens.domain.notification.NotificationRule;
import com.allcitizens.domain.notification.NotificationRuleRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.notification.mapper.NotificationRulePersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.notification.repository.JpaNotificationRuleRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class NotificationRuleRepositoryImpl implements NotificationRuleRepository {

    private final JpaNotificationRuleRepository jpaRepository;
    private final NotificationRulePersistenceMapper mapper;

    public NotificationRuleRepositoryImpl(
            JpaNotificationRuleRepository jpaRepository, NotificationRulePersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public NotificationRule save(NotificationRule rule) {
        var saved = jpaRepository.save(mapper.toEntity(rule));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<NotificationRule> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<NotificationRule> findByServiceId(UUID serviceId) {
        return jpaRepository.findByServiceIdOrderByEventAscChannelAsc(serviceId).stream()
                .map(mapper::toDomain)
                .toList();
    }
}
