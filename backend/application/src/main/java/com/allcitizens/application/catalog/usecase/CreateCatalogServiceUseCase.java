package com.allcitizens.application.catalog.usecase;

import com.allcitizens.application.catalog.command.CreateCatalogServiceCommand;
import com.allcitizens.application.catalog.result.CatalogServiceResult;

public interface CreateCatalogServiceUseCase {

    CatalogServiceResult execute(CreateCatalogServiceCommand command);
}
