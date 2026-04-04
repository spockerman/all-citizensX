package com.allcitizens.application.forwarding.result;

import com.allcitizens.domain.forwarding.ForwardingReason;

import java.util.UUID;

public record ForwardingReasonResult(UUID id, String name, String type) {

    public static ForwardingReasonResult fromDomain(ForwardingReason r) {
        return new ForwardingReasonResult(r.getId(), r.getName(), r.getType());
    }
}
