package com.allcitizens.application.catalog.usecase;

import com.allcitizens.application.catalog.result.CatalogServiceResult;

import java.util.List;
import java.util.UUID;

public interface ListCatalogServicesUseCase {

    List<CatalogServiceResult> execute(UUID tenantId);
}
