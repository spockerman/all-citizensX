package com.allcitizens.application.request.usecase;

import com.allcitizens.application.request.command.UpdateServiceRequestCommand;
import com.allcitizens.application.request.result.ServiceRequestResult;

import java.util.UUID;

public interface UpdateServiceRequestUseCase {

    ServiceRequestResult execute(UUID id, UpdateServiceRequestCommand command);
}
