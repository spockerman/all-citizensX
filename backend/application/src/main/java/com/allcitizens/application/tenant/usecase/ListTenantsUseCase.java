package com.allcitizens.application.tenant.usecase;

import com.allcitizens.application.tenant.query.ListTenantsQuery;
import com.allcitizens.application.tenant.result.TenantResult;
import com.allcitizens.domain.common.PageResult;

public interface ListTenantsUseCase {

    PageResult<TenantResult> execute(ListTenantsQuery query);
}
