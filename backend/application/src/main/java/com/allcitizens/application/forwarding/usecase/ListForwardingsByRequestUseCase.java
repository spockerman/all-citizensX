package com.allcitizens.application.forwarding.usecase;

import com.allcitizens.application.forwarding.result.ForwardingResult;

import java.util.List;
import java.util.UUID;

public interface ListForwardingsByRequestUseCase {

    List<ForwardingResult> execute(UUID requestId);
}
