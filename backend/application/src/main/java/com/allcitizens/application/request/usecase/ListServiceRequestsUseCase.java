package com.allcitizens.application.request.usecase;

import com.allcitizens.application.request.result.ServiceRequestResult;

import java.util.List;
import java.util.UUID;

public interface ListServiceRequestsUseCase {

    List<ServiceRequestResult> execute(UUID tenantId);
}
