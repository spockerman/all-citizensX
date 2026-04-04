package com.allcitizens.application.notification.command;

import java.util.UUID;

public record CreateNotificationRuleCommand(UUID serviceId, String event, String channel, String template) {
}
