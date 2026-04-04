package com.allcitizens.infrastructure.adapter.inbound.rest.attachment.mapper;

import com.allcitizens.application.attachment.command.CreateAttachmentCommand;
import com.allcitizens.application.attachment.result.AttachmentResult;
import com.allcitizens.domain.attachment.AttachmentType;
import com.allcitizens.infrastructure.adapter.inbound.rest.attachment.dto.AttachmentResponse;
import org.springframework.stereotype.Component;

@Component
public class AttachmentRestMapper {

    public AttachmentResponse toResponse(AttachmentResult r) {
        return new AttachmentResponse(
                r.id(),
                r.requestId(),
                r.type().name(),
                r.fileName(),
                r.contentType(),
                r.sizeBytes(),
                r.storagePath(),
                r.thumbnailPath(),
                r.userId(),
                r.createdAt()
        );
    }

    public CreateAttachmentCommand toCommand(
            java.util.UUID requestId,
            AttachmentType type,
            String fileName,
            String contentType,
            Long sizeBytes,
            String storagePath,
            String thumbnailPath,
            java.util.UUID userId) {
        return new CreateAttachmentCommand(
                requestId, type, fileName, contentType, sizeBytes, storagePath, thumbnailPath, userId
        );
    }
}
