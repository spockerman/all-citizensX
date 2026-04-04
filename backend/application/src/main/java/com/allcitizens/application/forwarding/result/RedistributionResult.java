package com.allcitizens.application.forwarding.result;

import com.allcitizens.domain.forwarding.Redistribution;
import com.allcitizens.domain.forwarding.ForwardingStatus;

import java.time.Instant;
import java.util.UUID;

public record RedistributionResult(
        UUID id,
        UUID forwardingId,
        UUID targetDepartmentId,
        UUID userId,
        ForwardingStatus status,
        boolean read,
        String notes,
        Instant createdAt
) {

    public static RedistributionResult fromDomain(Redistribution r) {
        return new RedistributionResult(
                r.getId(), r.getForwardingId(), r.getTargetDepartmentId(), r.getUserId(),
                r.getStatus(), r.isRead(), r.getNotes(), r.getCreatedAt()
        );
    }
}
