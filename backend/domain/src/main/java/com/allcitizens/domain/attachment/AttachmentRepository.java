package com.allcitizens.domain.attachment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AttachmentRepository {

    Attachment save(Attachment attachment);

    Optional<Attachment> findById(UUID id);

    List<Attachment> findAllByRequestId(UUID requestId);

    void deleteById(UUID id);
}
