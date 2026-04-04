package com.allcitizens.application.notification.result;

import com.allcitizens.domain.notification.NotificationRule;

import java.util.UUID;

public record NotificationRuleResult(
        UUID id, UUID serviceId, String event, String channel, String template, boolean active) {

    public static NotificationRuleResult fromDomain(NotificationRule r) {
        return new NotificationRuleResult(
                r.getId(),
                r.getServiceId(),
                r.getEvent(),
                r.getChannel().name(),
                r.getTemplate(),
                r.isActive());
    }
}
