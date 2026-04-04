package com.allcitizens.infrastructure.adapter.inbound.rest.forwarding.dto;

import java.util.UUID;

public record ForwardingReasonResponse(UUID id, String name, String type) {
}
