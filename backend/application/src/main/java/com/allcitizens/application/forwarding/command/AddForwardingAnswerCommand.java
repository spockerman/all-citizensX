package com.allcitizens.application.forwarding.command;

import java.util.UUID;

public record AddForwardingAnswerCommand(
        UUID forwardingId,
        UUID departmentId,
        UUID userId,
        UUID reasonId,
        String response
) {
}
