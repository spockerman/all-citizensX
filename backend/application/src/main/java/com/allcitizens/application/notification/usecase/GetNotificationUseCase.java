package com.allcitizens.application.notification.usecase;

import com.allcitizens.application.notification.result.NotificationResult;

import java.util.UUID;

@FunctionalInterface
public interface GetNotificationUseCase {

    NotificationResult execute(UUID id);
}
