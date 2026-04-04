package com.allcitizens.application.forwarding.usecase;

import com.allcitizens.application.forwarding.command.CreateRedistributionCommand;
import com.allcitizens.application.forwarding.result.RedistributionResult;

public interface CreateRedistributionUseCase {

    RedistributionResult execute(CreateRedistributionCommand command);
}
