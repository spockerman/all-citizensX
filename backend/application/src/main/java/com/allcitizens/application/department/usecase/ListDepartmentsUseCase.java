package com.allcitizens.application.department.usecase;

import com.allcitizens.application.department.result.DepartmentResult;

import java.util.List;
import java.util.UUID;

public interface ListDepartmentsUseCase {

    List<DepartmentResult> execute(UUID tenantId);
}
