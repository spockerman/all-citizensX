package com.allcitizens.application.attachment.usecase;

import com.allcitizens.application.attachment.command.CreateAttachmentCommand;
import com.allcitizens.application.attachment.result.AttachmentResult;

public interface CreateAttachmentUseCase {

    AttachmentResult execute(CreateAttachmentCommand command);
}
