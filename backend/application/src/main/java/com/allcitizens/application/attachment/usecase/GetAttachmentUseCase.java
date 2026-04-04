package com.allcitizens.application.attachment.usecase;

import com.allcitizens.application.attachment.result.AttachmentResult;

import java.util.UUID;

public interface GetAttachmentUseCase {

    AttachmentResult execute(UUID id);
}
