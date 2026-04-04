package com.allcitizens.application.notification.usecase;

import com.allcitizens.application.notification.command.UpdateNotificationRuleCommand;
import com.allcitizens.application.notification.result.NotificationRuleResult;

import java.util.UUID;

@FunctionalInterface
public interface UpdateNotificationRuleUseCase {

    NotificationRuleResult execute(UUID id, UpdateNotificationRuleCommand command);
}
