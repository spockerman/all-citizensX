package com.allcitizens.infrastructure.adapter.inbound.rest.notification.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record UpdateNotificationStatusRequest(
        @NotBlank String status, Instant sentAt, Instant readAt) {
}
