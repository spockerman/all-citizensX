package com.allcitizens.application.subdivision.usecase;

import com.allcitizens.application.subdivision.query.ListSubdivisionsQuery;
import com.allcitizens.application.subdivision.result.SubdivisionResult;
import com.allcitizens.domain.common.PageResult;

public interface ListSubdivisionsUseCase {

    PageResult<SubdivisionResult> execute(ListSubdivisionsQuery query);
}
