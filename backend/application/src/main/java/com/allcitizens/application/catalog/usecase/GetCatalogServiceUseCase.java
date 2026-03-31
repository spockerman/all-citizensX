package com.allcitizens.application.catalog.usecase;

import com.allcitizens.application.catalog.result.CatalogServiceResult;

import java.util.UUID;

public interface GetCatalogServiceUseCase {

    CatalogServiceResult execute(UUID id);
}
