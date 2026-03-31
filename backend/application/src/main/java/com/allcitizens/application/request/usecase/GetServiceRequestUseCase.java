package com.allcitizens.application.request.usecase;

import com.allcitizens.application.request.result.ServiceRequestResult;

import java.util.UUID;

public interface GetServiceRequestUseCase {

    ServiceRequestResult execute(UUID id);
}
