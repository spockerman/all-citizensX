package com.allcitizens.application.forwarding.usecase;

import com.allcitizens.application.forwarding.command.CreateForwardingReasonCommand;
import com.allcitizens.application.forwarding.result.ForwardingReasonResult;

public interface CreateForwardingReasonUseCase {

    ForwardingReasonResult execute(CreateForwardingReasonCommand command);
}
