package com.allcitizens.infrastructure.adapter.inbound.rest.subject.dto;

import java.util.UUID;

public record UpdateSubjectRequest(
    String name,
    UUID departmentId,
    Boolean active,
    Boolean visibleWeb,
    Boolean visibleApp
) {
}
