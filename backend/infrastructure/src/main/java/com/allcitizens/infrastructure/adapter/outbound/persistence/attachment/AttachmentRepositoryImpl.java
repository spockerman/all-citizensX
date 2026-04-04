package com.allcitizens.infrastructure.adapter.outbound.persistence.attachment;

import com.allcitizens.domain.attachment.Attachment;
import com.allcitizens.domain.attachment.AttachmentRepository;
import com.allcitizens.infrastructure.adapter.outbound.persistence.attachment.mapper.AttachmentPersistenceMapper;
import com.allcitizens.infrastructure.adapter.outbound.persistence.attachment.repository.JpaAttachmentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AttachmentRepositoryImpl implements AttachmentRepository {

    private final JpaAttachmentRepository jpa;
    private final AttachmentPersistenceMapper mapper;

    public AttachmentRepositoryImpl(JpaAttachmentRepository jpa, AttachmentPersistenceMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Attachment save(Attachment attachment) {
        return mapper.toDomain(jpa.save(mapper.toEntity(attachment)));
    }

    @Override
    public Optional<Attachment> findById(UUID id) {
        return jpa.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Attachment> findAllByRequestId(UUID requestId) {
        return jpa.findAllByRequestIdOrderByCreatedAtDesc(requestId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}
