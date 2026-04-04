package com.allcitizens.application.attachment.result;

import com.allcitizens.domain.attachment.Attachment;
import com.allcitizens.domain.attachment.AttachmentType;

import java.time.Instant;
import java.util.UUID;

public record AttachmentResult(
        UUID id,
        UUID requestId,
        AttachmentType type,
        String fileName,
        String contentType,
        Long sizeBytes,
        String storagePath,
        String thumbnailPath,
        UUID userId,
        Instant createdAt
) {

    public static AttachmentResult fromDomain(Attachment a) {
        return new AttachmentResult(
                a.getId(), a.getRequestId(), a.getType(), a.getFileName(), a.getContentType(),
                a.getSizeBytes(), a.getStoragePath(), a.getThumbnailPath(), a.getUserId(),
                a.getCreatedAt()
        );
    }
}
