package com.allcitizens.application.request.usecase;

import com.allcitizens.application.request.command.CreateServiceRequestCommand;
import com.allcitizens.application.request.result.ServiceRequestResult;

public interface CreateServiceRequestUseCase {

    ServiceRequestResult execute(CreateServiceRequestCommand command);
}
