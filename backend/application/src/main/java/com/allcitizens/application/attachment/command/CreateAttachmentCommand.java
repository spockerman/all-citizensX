package com.allcitizens.application.attachment.command;

import com.allcitizens.domain.attachment.AttachmentType;

import java.util.UUID;

public record CreateAttachmentCommand(
        UUID requestId,
        AttachmentType type,
        String fileName,
        String contentType,
        Long sizeBytes,
        String storagePath,
        String thumbnailPath,
        UUID userId
) {
}
