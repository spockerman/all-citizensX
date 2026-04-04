package com.allcitizens.application.catalog.usecase;

import com.allcitizens.application.catalog.query.ListCatalogServicesQuery;
import com.allcitizens.application.catalog.result.CatalogServiceResult;
import com.allcitizens.domain.common.PageResult;

public interface ListCatalogServicesUseCase {

    PageResult<CatalogServiceResult> execute(ListCatalogServicesQuery query);
}
