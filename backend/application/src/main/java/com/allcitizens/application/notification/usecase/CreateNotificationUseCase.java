package com.allcitizens.application.notification.usecase;

import com.allcitizens.application.notification.command.CreateNotificationCommand;
import com.allcitizens.application.notification.result.NotificationResult;

@FunctionalInterface
public interface CreateNotificationUseCase {

    NotificationResult execute(CreateNotificationCommand command);
}
