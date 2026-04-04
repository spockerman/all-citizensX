package com.allcitizens.domain.notification;

import com.allcitizens.domain.common.PageResult;

import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {

    Notification save(Notification notification);

    Optional<Notification> findById(UUID id);

    PageResult<Notification> findByTenantId(UUID tenantId, int page, int size);

    PageResult<Notification> findByTenantIdAndRequestId(UUID tenantId, UUID requestId, int page, int size);
}
