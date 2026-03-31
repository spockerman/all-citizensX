package com.allcitizens.application.catalog.usecase;

import com.allcitizens.application.catalog.command.UpdateCatalogServiceCommand;
import com.allcitizens.application.catalog.result.CatalogServiceResult;

import java.util.UUID;

public interface UpdateCatalogServiceUseCase {

    CatalogServiceResult execute(UUID id, UpdateCatalogServiceCommand command);
}
