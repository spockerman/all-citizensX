package com.allcitizens.application.request.command;

import com.allcitizens.domain.request.Channel;
import com.allcitizens.domain.request.Priority;

import java.util.Map;
import java.util.UUID;

public record CreateServiceRequestCommand(
        UUID tenantId,
        String protocol,
        UUID serviceId,
        UUID personId,
        UUID addressId,
        Channel channel,
        Priority priority,
        String description,
        boolean confidential,
        boolean anonymous,
        Map<String, Object> dynamicFields,
        Double latitude,
        Double longitude
) {
}
