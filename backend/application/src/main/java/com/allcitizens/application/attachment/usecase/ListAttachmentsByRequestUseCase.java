package com.allcitizens.application.attachment.usecase;

import com.allcitizens.application.attachment.result.AttachmentResult;

import java.util.List;
import java.util.UUID;

public interface ListAttachmentsByRequestUseCase {

    List<AttachmentResult> execute(UUID requestId);
}
