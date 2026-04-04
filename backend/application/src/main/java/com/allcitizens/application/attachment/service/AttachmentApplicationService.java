package com.allcitizens.application.attachment.service;

import com.allcitizens.application.attachment.command.CreateAttachmentCommand;
import com.allcitizens.application.attachment.result.AttachmentResult;
import com.allcitizens.domain.attachment.Attachment;
import com.allcitizens.domain.attachment.AttachmentRepository;
import com.allcitizens.domain.exception.EntityNotFoundException;
import com.allcitizens.domain.request.ServiceRequestRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

public class AttachmentApplicationService {

    private final AttachmentRepository attachmentRepository;
    private final ServiceRequestRepository serviceRequestRepository;

    public AttachmentApplicationService(
            AttachmentRepository attachmentRepository,
            ServiceRequestRepository serviceRequestRepository) {
        this.attachmentRepository = attachmentRepository;
        this.serviceRequestRepository = serviceRequestRepository;
    }

    @Transactional
    public AttachmentResult create(CreateAttachmentCommand command) {
        if (serviceRequestRepository.findById(command.requestId()).isEmpty()) {
            throw new EntityNotFoundException("ServiceRequest", command.requestId());
        }
        var attachment = Attachment.create(
                command.requestId(),
                command.type(),
                command.fileName(),
                command.contentType(),
                command.sizeBytes(),
                command.storagePath(),
                command.thumbnailPath(),
                command.userId()
        );
        var saved = attachmentRepository.save(attachment);
        return AttachmentResult.fromDomain(saved);
    }

    @Transactional
    public AttachmentResult getById(UUID id) {
        var a = attachmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Attachment", id));
        return AttachmentResult.fromDomain(a);
    }

    @Transactional
    public List<AttachmentResult> listByRequestId(UUID requestId) {
        if (serviceRequestRepository.findById(requestId).isEmpty()) {
            throw new EntityNotFoundException("ServiceRequest", requestId);
        }
        return attachmentRepository.findAllByRequestId(requestId).stream()
                .map(AttachmentResult::fromDomain)
                .toList();
    }

    @Transactional
    public void deleteById(UUID id) {
        if (attachmentRepository.findById(id).isEmpty()) {
            throw new EntityNotFoundException("Attachment", id);
        }
        attachmentRepository.deleteById(id);
    }
}
