package com.allcitizens.infrastructure.adapter.inbound.rest.attachment.dto;

import java.time.Instant;
import java.util.UUID;

public record AttachmentResponse(
        UUID id,
        UUID requestId,
        String type,
        String fileName,
        String contentType,
        Long sizeBytes,
        String storagePath,
        String thumbnailPath,
        UUID userId,
        Instant createdAt
) {
}
