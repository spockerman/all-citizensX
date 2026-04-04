package com.allcitizens.application.forwarding.usecase;

import com.allcitizens.application.forwarding.command.UpdateForwardingCommand;
import com.allcitizens.application.forwarding.result.ForwardingResult;

import java.util.UUID;

public interface UpdateForwardingUseCase {

    ForwardingResult execute(UUID id, UpdateForwardingCommand command);
}
