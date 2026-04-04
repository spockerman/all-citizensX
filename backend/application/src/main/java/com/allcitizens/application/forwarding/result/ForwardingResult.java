package com.allcitizens.application.forwarding.result;

import com.allcitizens.domain.forwarding.Forwarding;
import com.allcitizens.domain.forwarding.ForwardingStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record ForwardingResult(
        UUID id,
        UUID requestId,
        UUID targetDepartmentId,
        UUID sourceDepartmentId,
        UUID reasonId,
        UUID userId,
        ForwardingStatus status,
        String notes,
        LocalDate dueDate,
        boolean read,
        Instant readAt,
        Instant createdAt,
        Instant updatedAt,
        Instant answeredAt
) {

    public static ForwardingResult fromDomain(Forwarding f) {
        return new ForwardingResult(
                f.getId(), f.getRequestId(), f.getTargetDepartmentId(), f.getSourceDepartmentId(),
                f.getReasonId(), f.getUserId(), f.getStatus(), f.getNotes(), f.getDueDate(),
                f.isRead(), f.getReadAt(), f.getCreatedAt(), f.getUpdatedAt(), f.getAnsweredAt()
        );
    }
}
