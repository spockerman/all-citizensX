package com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto;

import java.time.Instant;

public record UpdateForwardingRequest(
        String status,
        Boolean read,
        Instant readAt,
        Instant answeredAt
) {
}
