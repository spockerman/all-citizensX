package com.allcitizens.application.notification.usecase;

import java.util.UUID;

@FunctionalInterface
public interface DeleteNotificationRuleUseCase {

    void delete(UUID id);
}
