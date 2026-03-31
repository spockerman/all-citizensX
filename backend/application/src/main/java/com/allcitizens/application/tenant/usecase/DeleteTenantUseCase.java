package com.allcitizens.application.tenant.usecase;

import java.util.UUID;

public interface DeleteTenantUseCase {

    void execute(UUID id);
}
