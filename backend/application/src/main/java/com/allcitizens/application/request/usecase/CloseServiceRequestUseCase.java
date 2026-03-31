package com.allcitizens.application.request.usecase;

import com.allcitizens.application.request.result.ServiceRequestResult;

import java.util.UUID;

public interface CloseServiceRequestUseCase {

    ServiceRequestResult execute(UUID id);
}
