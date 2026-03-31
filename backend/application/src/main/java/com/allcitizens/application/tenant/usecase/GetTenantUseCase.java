package com.allcitizens.application.tenant.usecase;

import com.allcitizens.application.tenant.result.TenantResult;

import java.util.UUID;

public interface GetTenantUseCase {

    TenantResult execute(UUID id);
}
