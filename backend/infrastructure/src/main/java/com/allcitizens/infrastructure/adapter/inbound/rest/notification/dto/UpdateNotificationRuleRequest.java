package com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto;

public record UpdateNotificationRuleRequest(
        String event, String channel, String template, Boolean active) {
}
