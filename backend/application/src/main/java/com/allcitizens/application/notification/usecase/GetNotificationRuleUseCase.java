package com.allcitizens.application.notification.usecase;

import com.allcitizens.application.notification.result.NotificationRuleResult;

import java.util.UUID;

@FunctionalInterface
public interface GetNotificationRuleUseCase {

    NotificationRuleResult execute(UUID id);
}
