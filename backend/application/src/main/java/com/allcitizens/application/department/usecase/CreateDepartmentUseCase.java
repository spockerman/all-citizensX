package com.allcitizens.application.department.usecase;

import com.allcitizens.application.department.command.CreateDepartmentCommand;
import com.allcitizens.application.department.result.DepartmentResult;

public interface CreateDepartmentUseCase {

    DepartmentResult execute(CreateDepartmentCommand command);
}
