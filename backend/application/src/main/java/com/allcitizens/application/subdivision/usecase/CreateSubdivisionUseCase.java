package com.allcitizens.application.subdivision.usecase;

import com.allcitizens.application.subdivision.command.CreateSubdivisionCommand;
import com.allcitizens.application.subdivision.result.SubdivisionResult;

public interface CreateSubdivisionUseCase {

    SubdivisionResult execute(CreateSubdivisionCommand command);
}
