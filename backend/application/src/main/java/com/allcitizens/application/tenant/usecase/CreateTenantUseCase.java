package com.allcitizens.application.tenant.usecase;

import com.allcitizens.application.tenant.command.CreateTenantCommand;
import com.allcitizens.application.tenant.result.TenantResult;

public interface CreateTenantUseCase {

    TenantResult execute(CreateTenantCommand command);
}
