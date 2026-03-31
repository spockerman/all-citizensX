package com.allcitizens.application.tenant.usecase;

import com.allcitizens.application.tenant.result.TenantResult;

import java.util.List;

public interface ListTenantsUseCase {

    List<TenantResult> execute();
}
