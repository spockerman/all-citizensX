package com.allcitizens.application.notification.usecase;

import com.allcitizens.application.notification.command.UpdateNotificationStatusCommand;
import com.allcitizens.application.notification.result.NotificationResult;

import java.util.UUID;

@FunctionalInterface
public interface UpdateNotificationStatusUseCase {

    NotificationResult execute(UUID id, UpdateNotificationStatusCommand command);
}
