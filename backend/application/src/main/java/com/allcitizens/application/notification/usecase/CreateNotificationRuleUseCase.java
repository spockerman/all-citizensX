package com.allcitizens.application.notification.usecase;

import com.allcitizens.application.notification.command.CreateNotificationRuleCommand;
import com.allcitizens.application.notification.result.NotificationRuleResult;

@FunctionalInterface
public interface CreateNotificationRuleUseCase {

    NotificationRuleResult execute(CreateNotificationRuleCommand command);
}
