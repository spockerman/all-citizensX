package com.allcitizens.application.notification.usecase;

import com.allcitizens.application.notification.result.NotificationRuleResult;

import java.util.List;
import java.util.UUID;

@FunctionalInterface
public interface ListNotificationRulesUseCase {

    List<NotificationRuleResult> listForService(UUID serviceId);
}
