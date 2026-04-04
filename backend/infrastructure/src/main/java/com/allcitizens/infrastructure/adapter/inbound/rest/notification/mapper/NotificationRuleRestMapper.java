package com.allcitizens.infrastructure.adapter.inbound.rest.notification.mapper;

import com.allcitizens.application.notification.command.CreateNotificationRuleCommand;
import com.allcitizens.application.notification.command.UpdateNotificationRuleCommand;
import com.allcitizens.application.notification.result.NotificationRuleResult;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto.CreateNotificationRuleRequest;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto.NotificationRuleResponse;
import com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto.UpdateNotificationRuleRequest;
import org.springframework.stereotype.Component;

@Component
public class NotificationRuleRestMapper {

    public CreateNotificationRuleCommand toCommand(CreateNotificationRuleRequest request) {
        return new CreateNotificationRuleCommand(
                request.serviceId(), request.event(), request.channel(), request.template());
    }

    public UpdateNotificationRuleCommand toCommand(UpdateNotificationRuleRequest request) {
        return new UpdateNotificationRuleCommand(request.event(), request.channel(), request.template(), request.active());
    }

    public NotificationRuleResponse toResponse(NotificationRuleResult r) {
        return new NotificationRuleResponse(
                r.id(), r.serviceId(), r.event(), r.channel(), r.template(), r.active());
    }
}
