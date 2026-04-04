package com.allcitizens.application.department.usecase;

import com.allcitizens.application.department.query.ListDepartmentsQuery;
import com.allcitizens.application.department.result.DepartmentResult;
import com.allcitizens.domain.common.PageResult;

public interface ListDepartmentsUseCase {

    PageResult<DepartmentResult> execute(ListDepartmentsQuery query);
}
