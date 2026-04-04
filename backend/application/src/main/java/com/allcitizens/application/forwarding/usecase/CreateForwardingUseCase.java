package com.allcitizens.application.forwarding.usecase;

import com.allcitizens.application.forwarding.command.CreateForwardingCommand;
import com.allcitizens.application.forwarding.result.ForwardingResult;

public interface CreateForwardingUseCase {

    ForwardingResult execute(CreateForwardingCommand command);
}
