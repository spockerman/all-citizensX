package com.allcitizens.application.forwarding.usecase;

import com.allcitizens.application.forwarding.result.ForwardingResult;

import java.util.UUID;

public interface GetForwardingUseCase {

    ForwardingResult execute(UUID id);
}
