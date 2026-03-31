package com.allcitizens.application.department.usecase;

import com.allcitizens.application.department.result.DepartmentResult;

import java.util.UUID;

public interface GetDepartmentUseCase {

    DepartmentResult execute(UUID id);
}
