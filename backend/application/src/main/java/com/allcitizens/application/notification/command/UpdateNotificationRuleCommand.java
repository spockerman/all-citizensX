package com.allcitizens.application.notification.command;

public record UpdateNotificationRuleCommand(
        String event, String channel, String template, Boolean active) {
}
