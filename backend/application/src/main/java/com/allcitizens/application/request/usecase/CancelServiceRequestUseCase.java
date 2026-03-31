package com.allcitizens.application.request.usecase;

import com.allcitizens.application.request.result.ServiceRequestResult;

import java.util.UUID;

public interface CancelServiceRequestUseCase {

    ServiceRequestResult execute(UUID id);
}
