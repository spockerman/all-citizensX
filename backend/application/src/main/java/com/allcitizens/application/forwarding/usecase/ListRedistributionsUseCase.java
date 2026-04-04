package com.allcitizens.application.forwarding.usecase;

import com.allcitizens.application.forwarding.result.RedistributionResult;

import java.util.List;
import java.util.UUID;

public interface ListRedistributionsUseCase {

    List<RedistributionResult> execute(UUID forwardingId);
}
