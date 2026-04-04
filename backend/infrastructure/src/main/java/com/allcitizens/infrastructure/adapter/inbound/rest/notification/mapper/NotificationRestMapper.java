package com.allcitizens.infrastructure.adapter.inbound.rest.notification.mapper;

import com.allcitizens.application.notification.command.CreateNotificationCommand;
import com.allcitizens.application.notification.command.UpdateNotificationStatusCommand;
import com.allcitizens.application.notification.result.NotificationResult;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto.CreateNotificationRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto.NotificationResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto.UpdateNotificationStatusRequest;
import org.springframework.stereotype.Component;

@Component
public class NotificationRestMapper {

    public CreateNotificationCommand toCommand(CreateNotificationRequest request) {
        return new CreateNotificationCommand(
                request.tenantId(),
                request.requestId(),
                request.recipientId(),
                request.channel(),
                request.title(),
                request.message(),
                request.extraData());
    }

    public UpdateNotificationStatusCommand toCommand(UpdateNotificationStatusRequest request) {
        return new UpdateNotificationStatusCommand(request.status(), request.sentAt(), request.readAt());
    }

    public NotificationResponse toResponse(NotificationResult r) {
        return new NotificationResponse(
                r.id(),
                r.tenantId(),
                r.requestId(),
                r.recipientId(),
                r.channel(),
                r.title(),
                r.message(),
                r.status(),
                r.extraData(),
                r.createdAt(),
                r.sentAt(),
                r.readAt());
    }
}
