package com.allcitizens.application.tenant.usecase;

import com.allcitizens.application.tenant.command.UpdateTenantCommand;
import com.allcitizens.application.tenant.result.TenantResult;

import java.util.UUID;

public interface UpdateTenantUseCase {

    TenantResult execute(UUID id, UpdateTenantCommand command);
}
