package com.allcitizens.domain.notification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRuleRepository {

    NotificationRule save(NotificationRule rule);

    Optional<NotificationRule> findById(UUID id);

    void deleteById(UUID id);

    List<NotificationRule> findByServiceId(UUID serviceId);
}
