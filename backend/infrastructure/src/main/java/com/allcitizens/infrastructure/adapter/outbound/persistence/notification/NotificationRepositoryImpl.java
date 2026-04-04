package com.allcitizens.infrastructure.adapter.outbound.persistence.notification;

import com.allcitizens.domain.common.PageResult;
import com.allcitizens.domain.notification.Notification;
import com.allcitizens.domain.notification.NotificationRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.notification.mapper.NotificationPersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.notification.repository.JpaNotificationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final JpaNotificationRepository jpaRepository;
    private final NotificationPersistenceMapper mapper;

    public NotificationRepositoryImpl(
            JpaNotificationRepository jpaRepository, NotificationPersistenceMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Notification save(Notification notification) {
        var saved = jpaRepository.save(mapper.toEntity(notification));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Notification> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public PageResult<Notification> findByTenantId(UUID tenantId, int page, int size) {
        var pg = jpaRepository.findByTenantIdOrderByCreatedAtDesc(tenantId, PageRequest.of(page, size));
        return toPageResult(pg);
    }

    @Override
    public PageResult<Notification> findByTenantIdAndRequestId(
            UUID tenantId, UUID requestId, int page, int size) {
        var pg = jpaRepository.findByTenantIdAndRequestIdOrderByCreatedAtDesc(
                tenantId, requestId, PageRequest.of(page, size));
        return toPageResult(pg);
    }

    private PageResult<Notification> toPageResult(
            org.springframework.data.domain.Page<com.allcitizens.infrastructure.adapter.outbound.persistence.notification.entity.NotificationJpaEntity> page) {
        var content = page.getContent().stream().map(mapper::toDomain).toList();
        return new PageResult<>(content, page.getTotalElements(), page.getNumber(), page.getSize());
    }
}
