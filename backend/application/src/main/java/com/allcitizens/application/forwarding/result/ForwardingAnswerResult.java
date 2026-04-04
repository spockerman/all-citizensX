package com.allcitizens.application.forwarding.result;

import com.allcitizens.domain.forwarding.ForwardingAnswer;

import java.time.Instant;
import java.util.UUID;

public record ForwardingAnswerResult(
        UUID id,
        UUID forwardingId,
        UUID departmentId,
        UUID userId,
        UUID reasonId,
        String response,
        Instant createdAt
) {

    public static ForwardingAnswerResult fromDomain(ForwardingAnswer a) {
        return new ForwardingAnswerResult(
                a.getId(), a.getForwardingId(), a.getDepartmentId(), a.getUserId(),
                a.getReasonId(), a.getResponse(), a.getCreatedAt()
        );
    }
}
