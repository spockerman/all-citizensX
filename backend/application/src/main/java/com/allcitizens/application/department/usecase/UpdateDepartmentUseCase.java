package com.allcitizens.application.department.usecase;

import com.allcitizens.application.department.command.UpdateDepartmentCommand;
import com.allcitizens.application.department.result.DepartmentResult;

import java.util.UUID;

public interface UpdateDepartmentUseCase {

    DepartmentResult execute(UUID id, UpdateDepartmentCommand command);
}
