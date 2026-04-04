package com.allcitizens.application.request.usecase;

import com.allcitizens.application.request.query.ListServiceRequestsQuery;
import com.allcitizens.application.request.result.ServiceRequestResult;
import com.allcitizens.domain.common.PageResult;

public interface ListServiceRequestsUseCase {

    PageResult<ServiceRequestResult> execute(ListServiceRequestsQuery query);
}
