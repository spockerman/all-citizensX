package com.allcitizens.application.notification.command;

import java.time.Instant;

public record UpdateNotificationStatusCommand(String status, Instant sentAt, Instant readAt) {
}
