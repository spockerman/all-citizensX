package com.allcitizens.infrastructure.adapter.outbound.persistence.attachment.mapper;

import com.allcitizens.domain.attachment.Attachment;
import com.allcitizens.domain.attachment.AttachmentType;
import com.allcitizens.infrastructure.adapter.outbound.persistence.attachment.entity.AttachmentJpaEntity;
import com.allcitizens.infrastructure.adapter.outbound.persistence.attachment.entity.AttachmentJpaEntity.AttachmentTypeJpa;
import org.springframework.stereotype.Component;

@Component
public class AttachmentPersistenceMapper {

    public Attachment toDomain(AttachmentJpaEntity e) {
        return Attachment.reconstitute(
                e.getId(),
                e.getRequestId(),
                AttachmentType.valueOf(e.getType().name()),
                e.getFileName(),
                e.getContentType(),
                e.getSizeBytes(),
                e.getStoragePath(),
                e.getThumbnailPath(),
                e.getUserId(),
                e.getCreatedAt()
        );
    }

    public AttachmentJpaEntity toEntity(Attachment d) {
        var e = new AttachmentJpaEntity();
        e.setId(d.getId());
        e.setRequestId(d.getRequestId());
        e.setType(AttachmentTypeJpa.valueOf(d.getType().name()));
        e.setFileName(d.getFileName());
        e.setContentType(d.getContentType());
        e.setSizeBytes(d.getSizeBytes());
        e.setStoragePath(d.getStoragePath());
        e.setThumbnailPath(d.getThumbnailPath());
        e.setUserId(d.getUserId());
        e.setCreatedAt(d.getCreatedAt());
        return e;
    }
}
