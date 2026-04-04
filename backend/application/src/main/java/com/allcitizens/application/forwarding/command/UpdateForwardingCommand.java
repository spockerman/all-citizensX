package com.allcitizens.application.forwarding.command;

import com.allcitizens.domain.forwarding.ForwardingStatus;

import java.time.Instant;

public record UpdateForwardingCommand(
        ForwardingStatus status,
        Boolean read,
        Instant readAt,
        Instant answeredAt
) {
}
