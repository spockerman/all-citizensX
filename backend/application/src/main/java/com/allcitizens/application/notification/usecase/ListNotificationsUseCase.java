package com.allcitizens.application.notification.usecase;

import com.allcitizens.application.notification.query.ListNotificationsQuery;
import com.allcitizens.application.notification.result.NotificationResult;
import com.allcitizens.domain.common.PageResult;

@FunctionalInterface
public interface ListNotificationsUseCase {

    PageResult<NotificationResult> execute(ListNotificationsQuery query);
}
